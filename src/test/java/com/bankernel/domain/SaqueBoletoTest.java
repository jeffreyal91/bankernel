package com.bankernel.domain;

import static com.bankernel.domain.SaqueBoletoTestSamples.*;
import static com.bankernel.domain.SaqueTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaqueBoletoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaqueBoleto.class);
        SaqueBoleto saqueBoleto1 = getSaqueBoletoSample1();
        SaqueBoleto saqueBoleto2 = new SaqueBoleto();
        assertThat(saqueBoleto1).isNotEqualTo(saqueBoleto2);

        saqueBoleto2.setId(saqueBoleto1.getId());
        assertThat(saqueBoleto1).isEqualTo(saqueBoleto2);

        saqueBoleto2 = getSaqueBoletoSample2();
        assertThat(saqueBoleto1).isNotEqualTo(saqueBoleto2);
    }

    @Test
    void saqueTest() {
        SaqueBoleto saqueBoleto = getSaqueBoletoRandomSampleGenerator();
        Saque saqueBack = getSaqueRandomSampleGenerator();

        saqueBoleto.setSaque(saqueBack);
        assertThat(saqueBoleto.getSaque()).isEqualTo(saqueBack);

        saqueBoleto.saque(null);
        assertThat(saqueBoleto.getSaque()).isNull();
    }
}
