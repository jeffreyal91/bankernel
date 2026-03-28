package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.ContaBancariaTestSamples.*;
import static com.bankernel.domain.DepositoBoletoTestSamples.*;
import static com.bankernel.domain.DepositoPixTestSamples.*;
import static com.bankernel.domain.DepositoTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepositoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deposito.class);
        Deposito deposito1 = getDepositoSample1();
        Deposito deposito2 = new Deposito();
        assertThat(deposito1).isNotEqualTo(deposito2);

        deposito2.setId(deposito1.getId());
        assertThat(deposito1).isEqualTo(deposito2);

        deposito2 = getDepositoSample2();
        assertThat(deposito1).isNotEqualTo(deposito2);
    }

    @Test
    void transacaoTest() {
        Deposito deposito = getDepositoRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        deposito.setTransacao(transacaoBack);
        assertThat(deposito.getTransacao()).isEqualTo(transacaoBack);

        deposito.transacao(null);
        assertThat(deposito.getTransacao()).isNull();
    }

    @Test
    void carteiraTest() {
        Deposito deposito = getDepositoRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        deposito.setCarteira(carteiraBack);
        assertThat(deposito.getCarteira()).isEqualTo(carteiraBack);

        deposito.carteira(null);
        assertThat(deposito.getCarteira()).isNull();
    }

    @Test
    void moedaCarteiraTest() {
        Deposito deposito = getDepositoRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        deposito.setMoedaCarteira(moedaCarteiraBack);
        assertThat(deposito.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        deposito.moedaCarteira(null);
        assertThat(deposito.getMoedaCarteira()).isNull();
    }

    @Test
    void contaBancariaTest() {
        Deposito deposito = getDepositoRandomSampleGenerator();
        ContaBancaria contaBancariaBack = getContaBancariaRandomSampleGenerator();

        deposito.setContaBancaria(contaBancariaBack);
        assertThat(deposito.getContaBancaria()).isEqualTo(contaBancariaBack);

        deposito.contaBancaria(null);
        assertThat(deposito.getContaBancaria()).isNull();
    }

    @Test
    void depositoPixTest() {
        Deposito deposito = getDepositoRandomSampleGenerator();
        DepositoPix depositoPixBack = getDepositoPixRandomSampleGenerator();

        deposito.setDepositoPix(depositoPixBack);
        assertThat(deposito.getDepositoPix()).isEqualTo(depositoPixBack);
        assertThat(depositoPixBack.getDeposito()).isEqualTo(deposito);

        deposito.depositoPix(null);
        assertThat(deposito.getDepositoPix()).isNull();
        assertThat(depositoPixBack.getDeposito()).isNull();
    }

    @Test
    void depositoBoletoTest() {
        Deposito deposito = getDepositoRandomSampleGenerator();
        DepositoBoleto depositoBoletoBack = getDepositoBoletoRandomSampleGenerator();

        deposito.setDepositoBoleto(depositoBoletoBack);
        assertThat(deposito.getDepositoBoleto()).isEqualTo(depositoBoletoBack);
        assertThat(depositoBoletoBack.getDeposito()).isEqualTo(deposito);

        deposito.depositoBoleto(null);
        assertThat(deposito.getDepositoBoleto()).isNull();
        assertThat(depositoBoletoBack.getDeposito()).isNull();
    }
}
