package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.CobrancaTestSamples.*;
import static com.bankernel.domain.LinkCobrancaTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CobrancaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cobranca.class);
        Cobranca cobranca1 = getCobrancaSample1();
        Cobranca cobranca2 = new Cobranca();
        assertThat(cobranca1).isNotEqualTo(cobranca2);

        cobranca2.setId(cobranca1.getId());
        assertThat(cobranca1).isEqualTo(cobranca2);

        cobranca2 = getCobrancaSample2();
        assertThat(cobranca1).isNotEqualTo(cobranca2);
    }

    @Test
    void transacaoTest() {
        Cobranca cobranca = getCobrancaRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        cobranca.setTransacao(transacaoBack);
        assertThat(cobranca.getTransacao()).isEqualTo(transacaoBack);

        cobranca.transacao(null);
        assertThat(cobranca.getTransacao()).isNull();
    }

    @Test
    void carteiraTest() {
        Cobranca cobranca = getCobrancaRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        cobranca.setCarteira(carteiraBack);
        assertThat(cobranca.getCarteira()).isEqualTo(carteiraBack);

        cobranca.carteira(null);
        assertThat(cobranca.getCarteira()).isNull();
    }

    @Test
    void carteiraCreditadaTest() {
        Cobranca cobranca = getCobrancaRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        cobranca.setCarteiraCreditada(carteiraBack);
        assertThat(cobranca.getCarteiraCreditada()).isEqualTo(carteiraBack);

        cobranca.carteiraCreditada(null);
        assertThat(cobranca.getCarteiraCreditada()).isNull();
    }

    @Test
    void moedaCarteiraTest() {
        Cobranca cobranca = getCobrancaRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        cobranca.setMoedaCarteira(moedaCarteiraBack);
        assertThat(cobranca.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        cobranca.moedaCarteira(null);
        assertThat(cobranca.getMoedaCarteira()).isNull();
    }

    @Test
    void linkCobrancaTest() {
        Cobranca cobranca = getCobrancaRandomSampleGenerator();
        LinkCobranca linkCobrancaBack = getLinkCobrancaRandomSampleGenerator();

        cobranca.setLinkCobranca(linkCobrancaBack);
        assertThat(cobranca.getLinkCobranca()).isEqualTo(linkCobrancaBack);

        cobranca.linkCobranca(null);
        assertThat(cobranca.getLinkCobranca()).isNull();
    }
}
