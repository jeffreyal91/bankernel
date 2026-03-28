package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.CheckoutTestSamples.*;
import static com.bankernel.domain.LinkPagamentoTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CheckoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Checkout.class);
        Checkout checkout1 = getCheckoutSample1();
        Checkout checkout2 = new Checkout();
        assertThat(checkout1).isNotEqualTo(checkout2);

        checkout2.setId(checkout1.getId());
        assertThat(checkout1).isEqualTo(checkout2);

        checkout2 = getCheckoutSample2();
        assertThat(checkout1).isNotEqualTo(checkout2);
    }

    @Test
    void transacaoTest() {
        Checkout checkout = getCheckoutRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        checkout.setTransacao(transacaoBack);
        assertThat(checkout.getTransacao()).isEqualTo(transacaoBack);

        checkout.transacao(null);
        assertThat(checkout.getTransacao()).isNull();
    }

    @Test
    void carteiraTest() {
        Checkout checkout = getCheckoutRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        checkout.setCarteira(carteiraBack);
        assertThat(checkout.getCarteira()).isEqualTo(carteiraBack);

        checkout.carteira(null);
        assertThat(checkout.getCarteira()).isNull();
    }

    @Test
    void carteiraCreditadaTest() {
        Checkout checkout = getCheckoutRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        checkout.setCarteiraCreditada(carteiraBack);
        assertThat(checkout.getCarteiraCreditada()).isEqualTo(carteiraBack);

        checkout.carteiraCreditada(null);
        assertThat(checkout.getCarteiraCreditada()).isNull();
    }

    @Test
    void moedaCarteiraTest() {
        Checkout checkout = getCheckoutRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        checkout.setMoedaCarteira(moedaCarteiraBack);
        assertThat(checkout.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        checkout.moedaCarteira(null);
        assertThat(checkout.getMoedaCarteira()).isNull();
    }

    @Test
    void linkPagamentoTest() {
        Checkout checkout = getCheckoutRandomSampleGenerator();
        LinkPagamento linkPagamentoBack = getLinkPagamentoRandomSampleGenerator();

        checkout.setLinkPagamento(linkPagamentoBack);
        assertThat(checkout.getLinkPagamento()).isEqualTo(linkPagamentoBack);

        checkout.linkPagamento(null);
        assertThat(checkout.getLinkPagamento()).isNull();
    }
}
