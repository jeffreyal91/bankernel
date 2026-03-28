package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarteiraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carteira.class);
        Carteira carteira1 = getCarteiraSample1();
        Carteira carteira2 = new Carteira();
        assertThat(carteira1).isNotEqualTo(carteira2);

        carteira2.setId(carteira1.getId());
        assertThat(carteira1).isEqualTo(carteira2);

        carteira2 = getCarteiraSample2();
        assertThat(carteira1).isNotEqualTo(carteira2);
    }

    @Test
    void moedaCarteiraTest() {
        Carteira carteira = getCarteiraRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        carteira.setMoedaCarteira(moedaCarteiraBack);
        assertThat(carteira.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        carteira.moedaCarteira(null);
        assertThat(carteira.getMoedaCarteira()).isNull();
    }
}
