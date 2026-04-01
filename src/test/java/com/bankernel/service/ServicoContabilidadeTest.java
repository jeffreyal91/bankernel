package com.bankernel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bankernel.domain.ContaContabil;
import com.bankernel.domain.LancamentoContabil;
import com.bankernel.domain.TipoOperacao;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.enumeration.EnumCategoriaContaContabil;
import com.bankernel.domain.enumeration.EnumSinalContabil;
import com.bankernel.domain.enumeration.EnumSinalLancamento;
import com.bankernel.domain.enumeration.EnumTipoContaContabil;
import com.bankernel.domain.enumeration.EnumTipoLancamento;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import com.bankernel.repository.ContaContabilRepository;
import com.bankernel.repository.LancamentoContabilRepository;
import com.bankernel.repository.TipoOperacaoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServicoContabilidadeTest {

    @Mock
    private TipoOperacaoRepository tipoOperacaoRepository;

    @Mock
    private ContaContabilRepository contaContabilRepository;

    @Mock
    private LancamentoContabilRepository lancamentoContabilRepository;

    @InjectMocks
    private ServicoContabilidade servicoContabilidade;

    private Transacao transacao;
    private TipoOperacao tipoOperacao;
    private ContaContabil contaDebito;
    private ContaContabil contaCredito;

    @BeforeEach
    void setUp() {
        transacao = new Transacao();
        transacao.setId(1L);

        contaDebito = new ContaContabil();
        contaDebito.setId(10L);
        contaDebito.setCodigo("1.1.001");
        contaDebito.setNome("Caixa");
        contaDebito.setSaldo(new BigDecimal("1000.00"));
        contaDebito.setAtiva(true);
        contaDebito.setTipoContaContabil(EnumTipoContaContabil.ATIVO);
        contaDebito.setCategoriaContaContabil(EnumCategoriaContaContabil.OPERACIONAL);

        contaCredito = new ContaContabil();
        contaCredito.setId(20L);
        contaCredito.setCodigo("2.1.001");
        contaCredito.setNome("Depositos a Vista");
        contaCredito.setSaldo(new BigDecimal("5000.00"));
        contaCredito.setAtiva(true);
        contaCredito.setTipoContaContabil(EnumTipoContaContabil.PASSIVO);
        contaCredito.setCategoriaContaContabil(EnumCategoriaContaContabil.OPERACIONAL);

        tipoOperacao = new TipoOperacao();
        tipoOperacao.setId(1L);
        tipoOperacao.setCodigo(EnumTipoOperacao.DEPOSITO_PIX);
        tipoOperacao.setNome("Deposito PIX");
        tipoOperacao.setAtivo(true);
        tipoOperacao.setSinalDebito(EnumSinalContabil.POSITIVO);
        tipoOperacao.setSinalCredito(EnumSinalContabil.POSITIVO);
        tipoOperacao.setContaDebito(contaDebito);
        tipoOperacao.setContaCredito(contaCredito);
    }

    @Test
    void contabilizar_deveCriarDoisLancamentosEAtualizarSaldos() {
        BigDecimal valor = new BigDecimal("250.00");

        when(tipoOperacaoRepository.findByCodigoComContas(EnumTipoOperacao.DEPOSITO_PIX))
            .thenReturn(Optional.of(tipoOperacao));
        when(contaContabilRepository.findByIdComBloqueio(10L)).thenReturn(Optional.of(contaDebito));
        when(contaContabilRepository.findByIdComBloqueio(20L)).thenReturn(Optional.of(contaCredito));
        when(lancamentoContabilRepository.save(any(LancamentoContabil.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(contaContabilRepository.save(any(ContaContabil.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        List<LancamentoContabil> resultado = servicoContabilidade.contabilizar(
            transacao, EnumTipoOperacao.DEPOSITO_PIX, valor);

        assertThat(resultado).hasSize(2);

        LancamentoContabil lancDebito = resultado.get(0);
        assertThat(lancDebito.getSinalLancamento()).isEqualTo(EnumSinalLancamento.DEBITO);
        assertThat(lancDebito.getValor()).isEqualByComparingTo(valor);
        assertThat(lancDebito.getTipoLancamento()).isEqualTo(EnumTipoLancamento.OPERACAO);
        assertThat(lancDebito.getAtivo()).isTrue();
        assertThat(lancDebito.getContaContabil().getId()).isEqualTo(10L);

        LancamentoContabil lancCredito = resultado.get(1);
        assertThat(lancCredito.getSinalLancamento()).isEqualTo(EnumSinalLancamento.CREDITO);
        assertThat(lancCredito.getValor()).isEqualByComparingTo(valor);
        assertThat(lancCredito.getContaContabil().getId()).isEqualTo(20L);

        // Saldos atualizados (ambos POSITIVO = soma)
        assertThat(contaDebito.getSaldo()).isEqualByComparingTo(new BigDecimal("1250.00"));
        assertThat(contaCredito.getSaldo()).isEqualByComparingTo(new BigDecimal("5250.00"));

        verify(lancamentoContabilRepository, times(2)).save(any(LancamentoContabil.class));
        verify(contaContabilRepository, times(2)).save(any(ContaContabil.class));
    }

    @Test
    void contabilizar_comSinalNegativo_deveSubtrairSaldo() {
        BigDecimal valor = new BigDecimal("100.00");

        tipoOperacao.setSinalDebito(EnumSinalContabil.NEGATIVO);
        tipoOperacao.setSinalCredito(EnumSinalContabil.POSITIVO);

        when(tipoOperacaoRepository.findByCodigoComContas(EnumTipoOperacao.DEPOSITO_PIX))
            .thenReturn(Optional.of(tipoOperacao));
        when(contaContabilRepository.findByIdComBloqueio(10L)).thenReturn(Optional.of(contaDebito));
        when(contaContabilRepository.findByIdComBloqueio(20L)).thenReturn(Optional.of(contaCredito));
        when(lancamentoContabilRepository.save(any(LancamentoContabil.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(contaContabilRepository.save(any(ContaContabil.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, valor);

        // Débito NEGATIVO = subtrai; Crédito POSITIVO = soma
        assertThat(contaDebito.getSaldo()).isEqualByComparingTo(new BigDecimal("900.00"));
        assertThat(contaCredito.getSaldo()).isEqualByComparingTo(new BigDecimal("5100.00"));
    }

    @Test
    void contabilizar_comMesmaContaDebitoCredito_deveAplicarAmbosAjustes() {
        BigDecimal valor = new BigDecimal("50.00");

        // Mesma conta para débito e crédito
        tipoOperacao.setContaCredito(contaDebito);
        tipoOperacao.setSinalDebito(EnumSinalContabil.POSITIVO);
        tipoOperacao.setSinalCredito(EnumSinalContabil.NEGATIVO);

        when(tipoOperacaoRepository.findByCodigoComContas(EnumTipoOperacao.DEPOSITO_PIX))
            .thenReturn(Optional.of(tipoOperacao));
        when(contaContabilRepository.findByIdComBloqueio(10L)).thenReturn(Optional.of(contaDebito));
        when(lancamentoContabilRepository.save(any(LancamentoContabil.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(contaContabilRepository.save(any(ContaContabil.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, valor);

        // POSITIVO (+50) + NEGATIVO (-50) = saldo original mantido
        assertThat(contaDebito.getSaldo()).isEqualByComparingTo(new BigDecimal("1000.00"));

        // Deve salvar a conta apenas uma vez (mesma conta)
        verify(contaContabilRepository, times(1)).save(any(ContaContabil.class));
    }

    @Test
    void contabilizar_comValorZero_deveLancarExcecao() {
        assertThatThrownBy(() ->
            servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, BigDecimal.ZERO))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("maior que zero");
    }

    @Test
    void contabilizar_comValorNegativo_deveLancarExcecao() {
        assertThatThrownBy(() ->
            servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, new BigDecimal("-10")))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("maior que zero");
    }

    @Test
    void contabilizar_comValorNulo_deveLancarExcecao() {
        assertThatThrownBy(() ->
            servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, null))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("maior que zero");
    }

    @Test
    void contabilizar_tipoOperacaoNaoEncontrado_deveLancarExcecao() {
        when(tipoOperacaoRepository.findByCodigoComContas(EnumTipoOperacao.DEPOSITO_PIX))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, new BigDecimal("100")))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("Tipo de operacao nao encontrado");
    }

    @Test
    void contabilizar_tipoOperacaoInativo_deveLancarExcecao() {
        tipoOperacao.setAtivo(false);

        when(tipoOperacaoRepository.findByCodigoComContas(EnumTipoOperacao.DEPOSITO_PIX))
            .thenReturn(Optional.of(tipoOperacao));

        assertThatThrownBy(() ->
            servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, new BigDecimal("100")))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("inativo");
    }

    @Test
    void contabilizar_contaDebitoNaoConfigurada_deveLancarExcecao() {
        tipoOperacao.setContaDebito(null);

        when(tipoOperacaoRepository.findByCodigoComContas(EnumTipoOperacao.DEPOSITO_PIX))
            .thenReturn(Optional.of(tipoOperacao));

        assertThatThrownBy(() ->
            servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, new BigDecimal("100")))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("conta de debito");
    }

    @Test
    void contabilizar_contaCreditoNaoConfigurada_deveLancarExcecao() {
        tipoOperacao.setContaCredito(null);

        when(tipoOperacaoRepository.findByCodigoComContas(EnumTipoOperacao.DEPOSITO_PIX))
            .thenReturn(Optional.of(tipoOperacao));

        assertThatThrownBy(() ->
            servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, new BigDecimal("100")))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("conta de credito");
    }

    @Test
    void contabilizar_contaInativa_deveLancarExcecao() {
        contaDebito.setAtiva(false);

        when(tipoOperacaoRepository.findByCodigoComContas(EnumTipoOperacao.DEPOSITO_PIX))
            .thenReturn(Optional.of(tipoOperacao));
        when(contaContabilRepository.findByIdComBloqueio(10L)).thenReturn(Optional.of(contaDebito));

        assertThatThrownBy(() ->
            servicoContabilidade.contabilizar(transacao, EnumTipoOperacao.DEPOSITO_PIX, new BigDecimal("100")))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("inativa");
    }

    @Test
    void estornar_deveInverterLancamentosEAtualizarSaldos() {
        // Lançamentos originais: Depósito PIX que somou em ATIVO(débito) e PASSIVO(crédito)
        LancamentoContabil lancOrigDebito = new LancamentoContabil()
            .valor(new BigDecimal("200.00"))
            .tipoLancamento(EnumTipoLancamento.OPERACAO)
            .sinalLancamento(EnumSinalLancamento.DEBITO)
            .ativo(true)
            .contaContabil(contaDebito);
        lancOrigDebito.setId(100L);

        LancamentoContabil lancOrigCredito = new LancamentoContabil()
            .valor(new BigDecimal("200.00"))
            .tipoLancamento(EnumTipoLancamento.OPERACAO)
            .sinalLancamento(EnumSinalLancamento.CREDITO)
            .ativo(true)
            .contaContabil(contaCredito);
        lancOrigCredito.setId(101L);

        when(lancamentoContabilRepository.findByTransacaoAndAtivoTrue(transacao))
            .thenReturn(List.of(lancOrigDebito, lancOrigCredito));
        when(contaContabilRepository.findByIdComBloqueio(10L)).thenReturn(Optional.of(contaDebito));
        when(contaContabilRepository.findByIdComBloqueio(20L)).thenReturn(Optional.of(contaCredito));
        when(lancamentoContabilRepository.save(any(LancamentoContabil.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(contaContabilRepository.save(any(ContaContabil.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        List<LancamentoContabil> estornos = servicoContabilidade.estornar(transacao);

        assertThat(estornos).hasSize(2);

        // Verifica que os originais foram desativados
        assertThat(lancOrigDebito.getAtivo()).isFalse();
        assertThat(lancOrigCredito.getAtivo()).isFalse();

        // Verifica os lançamentos de estorno
        LancamentoContabil estornoDebito = estornos.get(0);
        assertThat(estornoDebito.getTipoLancamento()).isEqualTo(EnumTipoLancamento.ESTORNO);
        assertThat(estornoDebito.getSinalLancamento()).isEqualTo(EnumSinalLancamento.CREDITO); // inverso de DEBITO
        assertThat(estornoDebito.getValor()).isEqualByComparingTo(new BigDecimal("200.00"));

        LancamentoContabil estornoCredito = estornos.get(1);
        assertThat(estornoCredito.getTipoLancamento()).isEqualTo(EnumTipoLancamento.ESTORNO);
        assertThat(estornoCredito.getSinalLancamento()).isEqualTo(EnumSinalLancamento.DEBITO); // inverso de CREDITO

        // contaDebito é ATIVO: DEBITO original somou → estorno subtrai
        assertThat(contaDebito.getSaldo()).isEqualByComparingTo(new BigDecimal("800.00"));
        // contaCredito é PASSIVO: CREDITO original somou → estorno subtrai
        assertThat(contaCredito.getSaldo()).isEqualByComparingTo(new BigDecimal("4800.00"));
    }

    @Test
    void estornar_semLancamentos_deveLancarExcecao() {
        when(lancamentoContabilRepository.findByTransacaoAndAtivoTrue(transacao))
            .thenReturn(List.of());

        assertThatThrownBy(() -> servicoContabilidade.estornar(transacao))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("Nenhum lancamento ativo");
    }

    @Test
    void estornar_quantidadeInconsistente_deveLancarExcecao() {
        LancamentoContabil unico = new LancamentoContabil()
            .valor(new BigDecimal("100.00"))
            .tipoLancamento(EnumTipoLancamento.OPERACAO)
            .sinalLancamento(EnumSinalLancamento.DEBITO)
            .ativo(true)
            .contaContabil(contaDebito);

        when(lancamentoContabilRepository.findByTransacaoAndAtivoTrue(transacao))
            .thenReturn(List.of(unico));

        assertThatThrownBy(() -> servicoContabilidade.estornar(transacao))
            .isInstanceOf(ContabilidadeException.class)
            .hasMessageContaining("Esperados 2 lancamentos");
    }
}
