package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.TransacaoTestSamples.*;
import static com.bankernel.domain.TransferenciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransferenciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transferencia.class);
        Transferencia transferencia1 = getTransferenciaSample1();
        Transferencia transferencia2 = new Transferencia();
        assertThat(transferencia1).isNotEqualTo(transferencia2);

        transferencia2.setId(transferencia1.getId());
        assertThat(transferencia1).isEqualTo(transferencia2);

        transferencia2 = getTransferenciaSample2();
        assertThat(transferencia1).isNotEqualTo(transferencia2);
    }

    @Test
    void transacaoTest() {
        Transferencia transferencia = getTransferenciaRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        transferencia.setTransacao(transacaoBack);
        assertThat(transferencia.getTransacao()).isEqualTo(transacaoBack);

        transferencia.transacao(null);
        assertThat(transferencia.getTransacao()).isNull();
    }

    @Test
    void carteiraOrigemTest() {
        Transferencia transferencia = getTransferenciaRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        transferencia.setCarteiraOrigem(carteiraBack);
        assertThat(transferencia.getCarteiraOrigem()).isEqualTo(carteiraBack);

        transferencia.carteiraOrigem(null);
        assertThat(transferencia.getCarteiraOrigem()).isNull();
    }

    @Test
    void carteiraDestinoTest() {
        Transferencia transferencia = getTransferenciaRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        transferencia.setCarteiraDestino(carteiraBack);
        assertThat(transferencia.getCarteiraDestino()).isEqualTo(carteiraBack);

        transferencia.carteiraDestino(null);
        assertThat(transferencia.getCarteiraDestino()).isNull();
    }

    @Test
    void moedaCarteiraTest() {
        Transferencia transferencia = getTransferenciaRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        transferencia.setMoedaCarteira(moedaCarteiraBack);
        assertThat(transferencia.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        transferencia.moedaCarteira(null);
        assertThat(transferencia.getMoedaCarteira()).isNull();
    }
}
