package com.bankernel.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bankernel.domain.Deposito;
import com.bankernel.domain.DepositoBoleto;
import com.bankernel.domain.DepositoPix;
import com.bankernel.domain.Saque;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsumidorContabilidadeTest {

    @Mock
    private ServicoContabilidade servicoContabilidade;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private DepositoRepository depositoRepository;

    @Mock
    private DepositoPixRepository depositoPixRepository;

    @Mock
    private DepositoBoletoRepository depositoBoletoRepository;

    @Mock
    private SaqueRepository saqueRepository;

    @Mock
    private CobrancaRepository cobrancaRepository;

    @Mock
    private ServicoMensageria servicoMensageria;

    @InjectMocks
    private ConsumidorContabilidade consumidorContabilidade;

    private Transacao transacao;

    @BeforeEach
    void setUp() {
        transacao = new Transacao();
        transacao.setId(1L);
    }

    private EventoDominio criarEvento(String tipoEntidade, Long idEntidade, String tipoOperacao, String valor) {
        return new EventoDominio(
            UUID.randomUUID().toString(),
            tipoOperacao + "_CONTABILIZAR",
            tipoEntidade,
            idEntidade,
            Map.of(
                "transacaoId", 1L,
                "tipoOperacao", tipoOperacao,
                "valor", valor
            ),
            Instant.now()
        );
    }

    @Test
    void processar_depositoPix_deveContabilizarEAtualizarFlag() throws Exception {
        EventoDominio evento = criarEvento("Deposito", 10L, "DEPOSITO_PIX", "500.00");

        Deposito deposito = new Deposito();
        deposito.setId(10L);
        deposito.setContabilizado(false);

        when(depositoRepository.findById(10L)).thenReturn(Optional.of(deposito));
        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));

        consumidorContabilidade.processar(evento);

        verify(servicoContabilidade).contabilizar(
            eq(transacao), eq(EnumTipoOperacao.DEPOSITO_PIX), eq(new BigDecimal("500.00")));
        verify(depositoRepository).save(deposito);
    }

    @Test
    void processar_saque_deveContabilizarEAtualizarFlag() throws Exception {
        EventoDominio evento = criarEvento("Saque", 20L, "SAQUE_PIX_CHAVE", "300.00");

        Saque saque = new Saque();
        saque.setId(20L);
        saque.setContabilizado(false);

        when(saqueRepository.findById(20L)).thenReturn(Optional.of(saque));
        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));

        consumidorContabilidade.processar(evento);

        verify(servicoContabilidade).contabilizar(
            eq(transacao), eq(EnumTipoOperacao.SAQUE_PIX_CHAVE), eq(new BigDecimal("300.00")));
        verify(saqueRepository).save(saque);
    }

    @Test
    void processar_jaContabilizado_deveIgnorar() throws Exception {
        EventoDominio evento = criarEvento("Deposito", 10L, "DEPOSITO_PIX", "500.00");

        Deposito deposito = new Deposito();
        deposito.setId(10L);
        deposito.setContabilizado(true);

        when(depositoRepository.findById(10L)).thenReturn(Optional.of(deposito));

        consumidorContabilidade.processar(evento);

        verify(servicoContabilidade, never()).contabilizar(any(), any(), any());
    }

    @Test
    void processar_depositoPix_deveAtualizarFlagNaEntidadeCorreta() throws Exception {
        EventoDominio evento = criarEvento("DepositoPix", 30L, "DEPOSITO_PIX", "200.00");

        DepositoPix depositoPix = new DepositoPix();
        depositoPix.setId(30L);
        depositoPix.setContabilizado(false);

        when(depositoPixRepository.findById(30L)).thenReturn(Optional.of(depositoPix));
        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));

        consumidorContabilidade.processar(evento);

        verify(servicoContabilidade).contabilizar(any(), any(), any());
        verify(depositoPixRepository).save(depositoPix);
    }

    @Test
    void processar_depositoBoleto_deveAtualizarFlagNaEntidadeCorreta() throws Exception {
        EventoDominio evento = criarEvento("DepositoBoleto", 40L, "DEPOSITO_BOLETO", "150.00");

        DepositoBoleto depositoBoleto = new DepositoBoleto();
        depositoBoleto.setId(40L);
        depositoBoleto.setContabilizado(false);

        when(depositoBoletoRepository.findById(40L)).thenReturn(Optional.of(depositoBoleto));
        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));

        consumidorContabilidade.processar(evento);

        verify(servicoContabilidade).contabilizar(any(), any(), any());
        verify(depositoBoletoRepository).save(depositoBoleto);
    }

    @Test
    void registrar_deveRegistrarConsumidorNaFila() {
        consumidorContabilidade.registrar();

        verify(servicoMensageria).registrarConsumidor(eq("contabilidade"), any());
    }
}
