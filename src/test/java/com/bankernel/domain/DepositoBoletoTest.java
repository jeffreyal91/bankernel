package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.DepositoBoletoTestSamples.*;
import static com.bankernel.domain.DepositoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepositoBoletoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositoBoleto.class);
        DepositoBoleto depositoBoleto1 = getDepositoBoletoSample1();
        DepositoBoleto depositoBoleto2 = new DepositoBoleto();
        assertThat(depositoBoleto1).isNotEqualTo(depositoBoleto2);

        depositoBoleto2.setId(depositoBoleto1.getId());
        assertThat(depositoBoleto1).isEqualTo(depositoBoleto2);

        depositoBoleto2 = getDepositoBoletoSample2();
        assertThat(depositoBoleto1).isNotEqualTo(depositoBoleto2);
    }

    @Test
    void depositoTest() {
        DepositoBoleto depositoBoleto = getDepositoBoletoRandomSampleGenerator();
        Deposito depositoBack = getDepositoRandomSampleGenerator();

        depositoBoleto.setDeposito(depositoBack);
        assertThat(depositoBoleto.getDeposito()).isEqualTo(depositoBack);

        depositoBoleto.deposito(null);
        assertThat(depositoBoleto.getDeposito()).isNull();
    }

    @Test
    void carteiraTest() {
        DepositoBoleto depositoBoleto = getDepositoBoletoRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        depositoBoleto.setCarteira(carteiraBack);
        assertThat(depositoBoleto.getCarteira()).isEqualTo(carteiraBack);

        depositoBoleto.carteira(null);
        assertThat(depositoBoleto.getCarteira()).isNull();
    }
}
