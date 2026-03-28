package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transacao.class);
        Transacao transacao1 = getTransacaoSample1();
        Transacao transacao2 = new Transacao();
        assertThat(transacao1).isNotEqualTo(transacao2);

        transacao2.setId(transacao1.getId());
        assertThat(transacao1).isEqualTo(transacao2);

        transacao2 = getTransacaoSample2();
        assertThat(transacao1).isNotEqualTo(transacao2);
    }

    @Test
    void carteiraOrigemTest() {
        Transacao transacao = getTransacaoRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        transacao.setCarteiraOrigem(carteiraBack);
        assertThat(transacao.getCarteiraOrigem()).isEqualTo(carteiraBack);

        transacao.carteiraOrigem(null);
        assertThat(transacao.getCarteiraOrigem()).isNull();
    }

    @Test
    void carteiraDestinoTest() {
        Transacao transacao = getTransacaoRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        transacao.setCarteiraDestino(carteiraBack);
        assertThat(transacao.getCarteiraDestino()).isEqualTo(carteiraBack);

        transacao.carteiraDestino(null);
        assertThat(transacao.getCarteiraDestino()).isNull();
    }

    @Test
    void moedaOrigemTest() {
        Transacao transacao = getTransacaoRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        transacao.setMoedaOrigem(moedaCarteiraBack);
        assertThat(transacao.getMoedaOrigem()).isEqualTo(moedaCarteiraBack);

        transacao.moedaOrigem(null);
        assertThat(transacao.getMoedaOrigem()).isNull();
    }

    @Test
    void moedaDestinoTest() {
        Transacao transacao = getTransacaoRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        transacao.setMoedaDestino(moedaCarteiraBack);
        assertThat(transacao.getMoedaDestino()).isEqualTo(moedaCarteiraBack);

        transacao.moedaDestino(null);
        assertThat(transacao.getMoedaDestino()).isNull();
    }
}
