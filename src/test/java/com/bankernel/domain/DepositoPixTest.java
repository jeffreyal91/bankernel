package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.DepositoPixTestSamples.*;
import static com.bankernel.domain.DepositoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepositoPixTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositoPix.class);
        DepositoPix depositoPix1 = getDepositoPixSample1();
        DepositoPix depositoPix2 = new DepositoPix();
        assertThat(depositoPix1).isNotEqualTo(depositoPix2);

        depositoPix2.setId(depositoPix1.getId());
        assertThat(depositoPix1).isEqualTo(depositoPix2);

        depositoPix2 = getDepositoPixSample2();
        assertThat(depositoPix1).isNotEqualTo(depositoPix2);
    }

    @Test
    void depositoTest() {
        DepositoPix depositoPix = getDepositoPixRandomSampleGenerator();
        Deposito depositoBack = getDepositoRandomSampleGenerator();

        depositoPix.setDeposito(depositoBack);
        assertThat(depositoPix.getDeposito()).isEqualTo(depositoBack);

        depositoPix.deposito(null);
        assertThat(depositoPix.getDeposito()).isNull();
    }

    @Test
    void carteiraTest() {
        DepositoPix depositoPix = getDepositoPixRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        depositoPix.setCarteira(carteiraBack);
        assertThat(depositoPix.getCarteira()).isEqualTo(carteiraBack);

        depositoPix.carteira(null);
        assertThat(depositoPix.getCarteira()).isNull();
    }
}
