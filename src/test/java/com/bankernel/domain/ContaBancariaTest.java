package com.bankernel.domain;

import static com.bankernel.domain.ContaBancariaTestSamples.*;
import static com.bankernel.domain.MoedaTestSamples.*;
import static com.bankernel.domain.PaisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaBancariaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaBancaria.class);
        ContaBancaria contaBancaria1 = getContaBancariaSample1();
        ContaBancaria contaBancaria2 = new ContaBancaria();
        assertThat(contaBancaria1).isNotEqualTo(contaBancaria2);

        contaBancaria2.setId(contaBancaria1.getId());
        assertThat(contaBancaria1).isEqualTo(contaBancaria2);

        contaBancaria2 = getContaBancariaSample2();
        assertThat(contaBancaria1).isNotEqualTo(contaBancaria2);
    }

    @Test
    void paisTest() {
        ContaBancaria contaBancaria = getContaBancariaRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        contaBancaria.setPais(paisBack);
        assertThat(contaBancaria.getPais()).isEqualTo(paisBack);

        contaBancaria.pais(null);
        assertThat(contaBancaria.getPais()).isNull();
    }

    @Test
    void moedaTest() {
        ContaBancaria contaBancaria = getContaBancariaRandomSampleGenerator();
        Moeda moedaBack = getMoedaRandomSampleGenerator();

        contaBancaria.setMoeda(moedaBack);
        assertThat(contaBancaria.getMoeda()).isEqualTo(moedaBack);

        contaBancaria.moeda(null);
        assertThat(contaBancaria.getMoeda()).isNull();
    }
}
