package com.bankernel.service;

import com.bankernel.domain.ContaContabil;
import com.bankernel.domain.LancamentoContabil;
import com.bankernel.domain.TipoOperacao;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.enumeration.EnumSinalContabil;
import com.bankernel.domain.enumeration.EnumSinalLancamento;
import com.bankernel.domain.enumeration.EnumTipoLancamento;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import com.bankernel.repository.ContaContabilRepository;
import com.bankernel.repository.LancamentoContabilRepository;
import com.bankernel.repository.TipoOperacaoRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço central de contabilidade - partida dobrada.
 *
 * <p>Trabalha no nível de entidades (não DTOs) pois é um serviço interno
 * chamado por outros serviços e pelo consumidor da fila de mensageria.</p>
 */
@Service
@Transactional
public class ServicoContabilidade {

    private static final Logger LOG = LoggerFactory.getLogger(ServicoContabilidade.class);

    private final TipoOperacaoRepository tipoOperacaoRepository;
    private final ContaContabilRepository contaContabilRepository;
    private final LancamentoContabilRepository lancamentoContabilRepository;

    public ServicoContabilidade(
        TipoOperacaoRepository tipoOperacaoRepository,
        ContaContabilRepository contaContabilRepository,
        LancamentoContabilRepository lancamentoContabilRepository
    ) {
        this.tipoOperacaoRepository = tipoOperacaoRepository;
        this.contaContabilRepository = contaContabilRepository;
        this.lancamentoContabilRepository = lancamentoContabilRepository;
    }

    /**
     * Contabiliza uma operação criando lançamentos de partida dobrada (débito + crédito).
     *
     * @param transacao a transação associada aos lançamentos
     * @param codigoOperacao o tipo da operação que define as contas e sinais
     * @param valor o valor monetário a ser lançado
     * @return lista com os dois lançamentos criados (débito e crédito)
     * @throws ContabilidadeException se valor inválido, tipo operação não encontrado/inativo, ou contas inativas
     */
    public List<LancamentoContabil> contabilizar(Transacao transacao, EnumTipoOperacao codigoOperacao, BigDecimal valor) {
        LOG.debug("Contabilizando operacao: tipo={}, valor={}, transacao={}", codigoOperacao, valor, transacao.getId());

        validarValor(valor);

        TipoOperacao tipoOperacao = tipoOperacaoRepository
            .findByCodigoComContas(codigoOperacao)
            .orElseThrow(() -> new ContabilidadeException(
                "Tipo de operacao nao encontrado: " + codigoOperacao, "TIPO_OPERACAO_NAO_ENCONTRADO"));

        validarTipoOperacao(tipoOperacao);

        // Lock das contas em ordem de ID para evitar deadlock
        ContaContabil contaDebito;
        ContaContabil contaCredito;

        if (tipoOperacao.getContaDebito().getId().equals(tipoOperacao.getContaCredito().getId())) {
            // Mesma conta para débito e crédito
            contaDebito = bloquearConta(tipoOperacao.getContaDebito().getId());
            contaCredito = contaDebito;
        } else {
            Long idMenor = Math.min(tipoOperacao.getContaDebito().getId(), tipoOperacao.getContaCredito().getId());
            Long idMaior = Math.max(tipoOperacao.getContaDebito().getId(), tipoOperacao.getContaCredito().getId());

            ContaContabil contaMenor = bloquearConta(idMenor);
            ContaContabil contaMaior = bloquearConta(idMaior);

            contaDebito = tipoOperacao.getContaDebito().getId().equals(idMenor) ? contaMenor : contaMaior;
            contaCredito = tipoOperacao.getContaCredito().getId().equals(idMenor) ? contaMenor : contaMaior;
        }

        // Criar lançamento de débito
        LancamentoContabil lancamentoDebito = new LancamentoContabil()
            .valor(valor)
            .tipoLancamento(EnumTipoLancamento.OPERACAO)
            .sinalLancamento(EnumSinalLancamento.DEBITO)
            .ativo(true)
            .contaContabil(contaDebito);
        lancamentoDebito.setTransacao(transacao);

        // Criar lançamento de crédito
        LancamentoContabil lancamentoCredito = new LancamentoContabil()
            .valor(valor)
            .tipoLancamento(EnumTipoLancamento.OPERACAO)
            .sinalLancamento(EnumSinalLancamento.CREDITO)
            .ativo(true)
            .contaContabil(contaCredito);
        lancamentoCredito.setTransacao(transacao);

        // Atualizar saldos com base nos sinais do TipoOperacao
        atualizarSaldo(contaDebito, valor, tipoOperacao.getSinalDebito());
        atualizarSaldo(contaCredito, valor, tipoOperacao.getSinalCredito());

        lancamentoContabilRepository.save(lancamentoDebito);
        lancamentoContabilRepository.save(lancamentoCredito);
        contaContabilRepository.save(contaDebito);
        if (!contaDebito.getId().equals(contaCredito.getId())) {
            contaContabilRepository.save(contaCredito);
        }

        LOG.info("Contabilizacao realizada: transacao={}, debito conta={}, credito conta={}, valor={}",
            transacao.getId(), contaDebito.getCodigo(), contaCredito.getCodigo(), valor);

        return List.of(lancamentoDebito, lancamentoCredito);
    }

    /**
     * Estorna os lançamentos contábeis de uma transação, criando lançamentos inversos.
     *
     * @param transacao a transação cujos lançamentos serão estornados
     * @return lista com os lançamentos de estorno criados
     * @throws ContabilidadeException se não encontrar lançamentos ou a quantidade for inconsistente
     */
    public List<LancamentoContabil> estornar(Transacao transacao) {
        LOG.debug("Estornando lancamentos da transacao: {}", transacao.getId());

        List<LancamentoContabil> originais = lancamentoContabilRepository.findByTransacaoAndAtivoTrue(transacao);

        if (originais.isEmpty()) {
            throw new ContabilidadeException(
                "Nenhum lancamento ativo encontrado para a transacao: " + transacao.getId(),
                "LANCAMENTOS_NAO_ENCONTRADOS");
        }

        if (originais.size() != 2) {
            throw new ContabilidadeException(
                "Esperados 2 lancamentos ativos, encontrados " + originais.size() + " para transacao: " + transacao.getId(),
                "LANCAMENTOS_INCONSISTENTES");
        }

        // Identificar contas envolvidas e fazer lock em ordem de ID
        List<Long> idsContas = originais.stream()
            .map(l -> l.getContaContabil().getId())
            .distinct()
            .sorted()
            .toList();

        List<ContaContabil> contasBloqueadas = new ArrayList<>();
        for (Long id : idsContas) {
            contasBloqueadas.add(bloquearConta(id));
        }

        List<LancamentoContabil> estornos = new ArrayList<>();

        for (LancamentoContabil original : originais) {
            // Sinal inverso
            EnumSinalLancamento sinalInverso = original.getSinalLancamento() == EnumSinalLancamento.DEBITO
                ? EnumSinalLancamento.CREDITO
                : EnumSinalLancamento.DEBITO;

            // Buscar a conta bloqueada correspondente
            ContaContabil contaBloqueada = contasBloqueadas.stream()
                .filter(c -> c.getId().equals(original.getContaContabil().getId()))
                .findFirst()
                .orElseThrow();

            LancamentoContabil estorno = new LancamentoContabil()
                .valor(original.getValor())
                .tipoLancamento(EnumTipoLancamento.ESTORNO)
                .sinalLancamento(sinalInverso)
                .ativo(true)
                .contaContabil(contaBloqueada);
            estorno.setTransacao(transacao);

            // Reverter o saldo: o estorno faz o oposto do original.
            // Se o original adicionou (POSITIVO), o estorno subtrai; e vice-versa.
            // Determinamos a direção original pela combinação sinal do lançamento + sinal contábil do tipo operação.
            // Simplificação: o estorno inverte o sinal do lançamento, então invertemos o efeito no saldo.
            // Se DEBITO original aumentou o saldo → CREDITO estorno diminui (e vice-versa)
            // Basta aplicar o sinal inverso: se o original era POSITIVO no saldo, agora é NEGATIVO
            if (original.getSinalLancamento() == EnumSinalLancamento.DEBITO) {
                // O débito original afetou com algum sinal; o estorno reverte
                // Precisamos saber se o débito original somou ou subtraiu
                // Como não temos o TipoOperacao aqui, usamos a abordagem direta:
                // O valor original foi +valor ou -valor no saldo.
                // Para reverter, aplicamos o oposto.
                reverterSaldo(contaBloqueada, original.getValor(), original.getSinalLancamento());
            } else {
                reverterSaldo(contaBloqueada, original.getValor(), original.getSinalLancamento());
            }

            // Marcar original como inativo
            original.setAtivo(false);
            lancamentoContabilRepository.save(original);

            lancamentoContabilRepository.save(estorno);
            estornos.add(estorno);
        }

        // Salvar contas com saldos atualizados
        for (ContaContabil conta : contasBloqueadas) {
            contaContabilRepository.save(conta);
        }

        LOG.info("Estorno realizado: transacao={}, {} lancamentos estornados", transacao.getId(), estornos.size());

        return estornos;
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ContabilidadeException("Valor deve ser maior que zero", "VALOR_INVALIDO");
        }
    }

    private void validarTipoOperacao(TipoOperacao tipoOperacao) {
        if (!Boolean.TRUE.equals(tipoOperacao.getAtivo())) {
            throw new ContabilidadeException(
                "Tipo de operacao inativo: " + tipoOperacao.getCodigo(), "TIPO_OPERACAO_INATIVO");
        }
        if (tipoOperacao.getContaDebito() == null) {
            throw new ContabilidadeException(
                "Tipo de operacao sem conta de debito: " + tipoOperacao.getCodigo(), "CONTA_DEBITO_NAO_CONFIGURADA");
        }
        if (tipoOperacao.getContaCredito() == null) {
            throw new ContabilidadeException(
                "Tipo de operacao sem conta de credito: " + tipoOperacao.getCodigo(), "CONTA_CREDITO_NAO_CONFIGURADA");
        }
    }

    private ContaContabil bloquearConta(Long id) {
        ContaContabil conta = contaContabilRepository.findByIdComBloqueio(id)
            .orElseThrow(() -> new ContabilidadeException("Conta contabil nao encontrada: " + id, "CONTA_NAO_ENCONTRADA"));

        if (!Boolean.TRUE.equals(conta.getAtiva())) {
            throw new ContabilidadeException("Conta contabil inativa: " + conta.getCodigo(), "CONTA_INATIVA");
        }

        return conta;
    }

    private void atualizarSaldo(ContaContabil conta, BigDecimal valor, EnumSinalContabil sinal) {
        if (sinal == EnumSinalContabil.POSITIVO) {
            conta.setSaldo(conta.getSaldo().add(valor));
        } else {
            conta.setSaldo(conta.getSaldo().subtract(valor));
        }
    }

    /**
     * Reverte o efeito de um lançamento no saldo da conta.
     * Para reverter, precisamos desfazer o que o lançamento original fez.
     * Como o sinal original foi determinado pelo TipoOperacao (que não temos aqui),
     * calculamos a diferença entre o saldo antes e depois do lançamento.
     *
     * Abordagem simplificada: o estorno simplesmente soma o valor se o original subtraiu,
     * e subtrai se o original somou. Como não sabemos o sinal original, revertemos
     * baseado no tipo de conta contábil:
     * - ATIVO/DESPESA: DEBITO soma, CREDITO subtrai → estorno de DEBITO subtrai, estorno de CREDITO soma
     * - PASSIVO/RECEITA: CREDITO soma, DEBITO subtrai → estorno de CREDITO subtrai, estorno de DEBITO soma
     */
    private void reverterSaldo(ContaContabil conta, BigDecimal valor, EnumSinalLancamento sinalOriginal) {
        boolean debitoOriginal = sinalOriginal == EnumSinalLancamento.DEBITO;

        switch (conta.getTipoContaContabil()) {
            case ATIVO, DESPESA -> {
                // Débito original somou → estorno subtrai; Crédito original subtraiu → estorno soma
                if (debitoOriginal) {
                    conta.setSaldo(conta.getSaldo().subtract(valor));
                } else {
                    conta.setSaldo(conta.getSaldo().add(valor));
                }
            }
            case PASSIVO, RECEITA -> {
                // Crédito original somou → estorno subtrai; Débito original subtraiu → estorno soma
                if (debitoOriginal) {
                    conta.setSaldo(conta.getSaldo().add(valor));
                } else {
                    conta.setSaldo(conta.getSaldo().subtract(valor));
                }
            }
        }
    }
}
