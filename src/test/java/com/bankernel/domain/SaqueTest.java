package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.ContaBancariaTestSamples.*;
import static com.bankernel.domain.EscritorioTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.SaqueBoletoTestSamples.*;
import static com.bankernel.domain.SaquePixTestSamples.*;
import static com.bankernel.domain.SaqueTestSamples.*;
import static com.bankernel.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Saque.class);
        Saque saque1 = getSaqueSample1();
        Saque saque2 = new Saque();
        assertThat(saque1).isNotEqualTo(saque2);

        saque2.setId(saque1.getId());
        assertThat(saque1).isEqualTo(saque2);

        saque2 = getSaqueSample2();
        assertThat(saque1).isNotEqualTo(saque2);
    }

    @Test
    void transacaoTest() {
        Saque saque = getSaqueRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        saque.setTransacao(transacaoBack);
        assertThat(saque.getTransacao()).isEqualTo(transacaoBack);

        saque.transacao(null);
        assertThat(saque.getTransacao()).isNull();
    }

    @Test
    void transacaoEstornoTest() {
        Saque saque = getSaqueRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        saque.setTransacaoEstorno(transacaoBack);
        assertThat(saque.getTransacaoEstorno()).isEqualTo(transacaoBack);

        saque.transacaoEstorno(null);
        assertThat(saque.getTransacaoEstorno()).isNull();
    }

    @Test
    void carteiraTest() {
        Saque saque = getSaqueRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        saque.setCarteira(carteiraBack);
        assertThat(saque.getCarteira()).isEqualTo(carteiraBack);

        saque.carteira(null);
        assertThat(saque.getCarteira()).isNull();
    }

    @Test
    void moedaCarteiraTest() {
        Saque saque = getSaqueRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        saque.setMoedaCarteira(moedaCarteiraBack);
        assertThat(saque.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        saque.moedaCarteira(null);
        assertThat(saque.getMoedaCarteira()).isNull();
    }

    @Test
    void contaBancariaDestinoTest() {
        Saque saque = getSaqueRandomSampleGenerator();
        ContaBancaria contaBancariaBack = getContaBancariaRandomSampleGenerator();

        saque.setContaBancariaDestino(contaBancariaBack);
        assertThat(saque.getContaBancariaDestino()).isEqualTo(contaBancariaBack);

        saque.contaBancariaDestino(null);
        assertThat(saque.getContaBancariaDestino()).isNull();
    }

    @Test
    void escritorioTest() {
        Saque saque = getSaqueRandomSampleGenerator();
        Escritorio escritorioBack = getEscritorioRandomSampleGenerator();

        saque.setEscritorio(escritorioBack);
        assertThat(saque.getEscritorio()).isEqualTo(escritorioBack);

        saque.escritorio(null);
        assertThat(saque.getEscritorio()).isNull();
    }

    @Test
    void saquePixTest() {
        Saque saque = getSaqueRandomSampleGenerator();
        SaquePix saquePixBack = getSaquePixRandomSampleGenerator();

        saque.setSaquePix(saquePixBack);
        assertThat(saque.getSaquePix()).isEqualTo(saquePixBack);
        assertThat(saquePixBack.getSaque()).isEqualTo(saque);

        saque.saquePix(null);
        assertThat(saque.getSaquePix()).isNull();
        assertThat(saquePixBack.getSaque()).isNull();
    }

    @Test
    void saqueBoletoTest() {
        Saque saque = getSaqueRandomSampleGenerator();
        SaqueBoleto saqueBoletoBack = getSaqueBoletoRandomSampleGenerator();

        saque.setSaqueBoleto(saqueBoletoBack);
        assertThat(saque.getSaqueBoleto()).isEqualTo(saqueBoletoBack);
        assertThat(saqueBoletoBack.getSaque()).isEqualTo(saque);

        saque.saqueBoleto(null);
        assertThat(saque.getSaqueBoleto()).isNull();
        assertThat(saqueBoletoBack.getSaque()).isNull();
    }
}
