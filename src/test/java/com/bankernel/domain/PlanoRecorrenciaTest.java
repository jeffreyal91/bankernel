package com.bankernel.domain;

import static com.bankernel.domain.LinkCobrancaTestSamples.*;
import static com.bankernel.domain.PlanoRecorrenciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanoRecorrenciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoRecorrencia.class);
        PlanoRecorrencia planoRecorrencia1 = getPlanoRecorrenciaSample1();
        PlanoRecorrencia planoRecorrencia2 = new PlanoRecorrencia();
        assertThat(planoRecorrencia1).isNotEqualTo(planoRecorrencia2);

        planoRecorrencia2.setId(planoRecorrencia1.getId());
        assertThat(planoRecorrencia1).isEqualTo(planoRecorrencia2);

        planoRecorrencia2 = getPlanoRecorrenciaSample2();
        assertThat(planoRecorrencia1).isNotEqualTo(planoRecorrencia2);
    }

    @Test
    void linkCobrancaTest() {
        PlanoRecorrencia planoRecorrencia = getPlanoRecorrenciaRandomSampleGenerator();
        LinkCobranca linkCobrancaBack = getLinkCobrancaRandomSampleGenerator();

        planoRecorrencia.setLinkCobranca(linkCobrancaBack);
        assertThat(planoRecorrencia.getLinkCobranca()).isEqualTo(linkCobrancaBack);

        planoRecorrencia.linkCobranca(null);
        assertThat(planoRecorrencia.getLinkCobranca()).isNull();
    }
}
