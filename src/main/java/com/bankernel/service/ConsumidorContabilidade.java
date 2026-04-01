package com.bankernel.service;

import com.bankernel.domain.Transacao;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import com.bankernel.repository.CobrancaRepository;
import com.bankernel.repository.DepositoBoletoRepository;
import com.bankernel.repository.DepositoPixRepository;
import com.bankernel.repository.DepositoRepository;
import com.bankernel.repository.SaqueRepository;
import com.bankernel.repository.TransacaoRepository;
import com.bankernel.service.integracao.mensageria.EventoDominio;
import com.bankernel.service.integracao.mensageria.ServicoMensageria;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consumidor da fila "contabilidade" da mensageria.
 *
 * <p>Processa eventos de operações financeiras e gera os lançamentos contábeis
 * de partida dobrada correspondentes. Atualiza o flag {@code contabilizado}
 * na entidade de origem após a contabilização.</p>
 *
 * <p>O processamento é idempotente: se a entidade já estiver contabilizada,
 * o evento é ignorado.</p>
 */
@Service
public class ConsumidorContabilidade {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumidorContabilidade.class);
    private static final String FILA_CONTABILIDADE = "contabilidade";

    private final ServicoContabilidade servicoContabilidade;
    private final TransacaoRepository transacaoRepository;
    private final DepositoRepository depositoRepository;
    private final DepositoPixRepository depositoPixRepository;
    private final DepositoBoletoRepository depositoBoletoRepository;
    private final SaqueRepository saqueRepository;
    private final CobrancaRepository cobrancaRepository;
    private final ServicoMensageria servicoMensageria;

    public ConsumidorContabilidade(
        ServicoContabilidade servicoContabilidade,
        TransacaoRepository transacaoRepository,
        DepositoRepository depositoRepository,
        DepositoPixRepository depositoPixRepository,
        DepositoBoletoRepository depositoBoletoRepository,
        SaqueRepository saqueRepository,
        CobrancaRepository cobrancaRepository,
        ServicoMensageria servicoMensageria
    ) {
        this.servicoContabilidade = servicoContabilidade;
        this.transacaoRepository = transacaoRepository;
        this.depositoRepository = depositoRepository;
        this.depositoPixRepository = depositoPixRepository;
        this.depositoBoletoRepository = depositoBoletoRepository;
        this.saqueRepository = saqueRepository;
        this.cobrancaRepository = cobrancaRepository;
        this.servicoMensageria = servicoMensageria;
    }

    @PostConstruct
    public void registrar() {
        servicoMensageria.registrarConsumidor(FILA_CONTABILIDADE, this::processar);
        LOG.info("Consumidor registrado na fila: {}", FILA_CONTABILIDADE);
    }

    /**
     * Processa um evento contábil da fila de mensageria.
     *
     * <p>Contrato do evento ({@code dados}):</p>
     * <ul>
     *   <li>{@code transacaoId} (Long) - ID da Transação associada</li>
     *   <li>{@code tipoOperacao} (String) - Nome do EnumTipoOperacao</li>
     *   <li>{@code valor} (String) - BigDecimal como string</li>
     * </ul>
     */
    @Transactional
    public void processar(EventoDominio evento) throws Exception {
        LOG.info("Processando evento contabil: tipo={}, entidade={}, id={}",
            evento.tipo(), evento.tipoEntidade(), evento.idEntidade());

        // Verificar idempotência antes de processar
        if (jaContabilizado(evento.tipoEntidade(), evento.idEntidade())) {
            LOG.warn("Entidade ja contabilizada, ignorando evento: tipo={}, id={}",
                evento.tipoEntidade(), evento.idEntidade());
            return;
        }

        Long transacaoId = ((Number) evento.dados().get("transacaoId")).longValue();
        String tipoOperacaoStr = (String) evento.dados().get("tipoOperacao");
        BigDecimal valor = new BigDecimal(evento.dados().get("valor").toString());

        EnumTipoOperacao tipoOperacao = EnumTipoOperacao.valueOf(tipoOperacaoStr);

        Transacao transacao = transacaoRepository.findById(transacaoId)
            .orElseThrow(() -> new ContabilidadeException(
                "Transacao nao encontrada: " + transacaoId, "TRANSACAO_NAO_ENCONTRADA"));

        servicoContabilidade.contabilizar(transacao, tipoOperacao, valor);

        atualizarContabilizado(evento.tipoEntidade(), evento.idEntidade());

        LOG.info("Evento contabil processado com sucesso: tipo={}, entidade={}, id={}",
            evento.tipo(), evento.tipoEntidade(), evento.idEntidade());
    }

    private boolean jaContabilizado(String tipoEntidade, Long idEntidade) {
        return switch (tipoEntidade) {
            case "Deposito" -> depositoRepository.findById(idEntidade)
                .map(d -> Boolean.TRUE.equals(d.getContabilizado()))
                .orElse(false);
            case "DepositoPix" -> depositoPixRepository.findById(idEntidade)
                .map(d -> Boolean.TRUE.equals(d.getContabilizado()))
                .orElse(false);
            case "DepositoBoleto" -> depositoBoletoRepository.findById(idEntidade)
                .map(d -> Boolean.TRUE.equals(d.getContabilizado()))
                .orElse(false);
            case "Saque" -> saqueRepository.findById(idEntidade)
                .map(s -> Boolean.TRUE.equals(s.getContabilizado()))
                .orElse(false);
            case "Cobranca" -> cobrancaRepository.findById(idEntidade)
                .map(c -> Boolean.TRUE.equals(c.getContabilizado()))
                .orElse(false);
            default -> false;
        };
    }

    private void atualizarContabilizado(String tipoEntidade, Long idEntidade) {
        switch (tipoEntidade) {
            case "Deposito" -> depositoRepository.findById(idEntidade)
                .ifPresent(d -> {
                    d.setContabilizado(true);
                    depositoRepository.save(d);
                });
            case "DepositoPix" -> depositoPixRepository.findById(idEntidade)
                .ifPresent(d -> {
                    d.setContabilizado(true);
                    depositoPixRepository.save(d);
                });
            case "DepositoBoleto" -> depositoBoletoRepository.findById(idEntidade)
                .ifPresent(d -> {
                    d.setContabilizado(true);
                    depositoBoletoRepository.save(d);
                });
            case "Saque" -> saqueRepository.findById(idEntidade)
                .ifPresent(s -> {
                    s.setContabilizado(true);
                    saqueRepository.save(s);
                });
            case "Cobranca" -> cobrancaRepository.findById(idEntidade)
                .ifPresent(c -> {
                    c.setContabilizado(true);
                    cobrancaRepository.save(c);
                });
            default -> LOG.warn("Tipo entidade desconhecido para contabilizado: {}", tipoEntidade);
        }
    }
}
