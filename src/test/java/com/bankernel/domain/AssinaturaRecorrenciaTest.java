package com.bankernel.domain;

import static com.bankernel.domain.AssinaturaRecorrenciaTestSamples.*;
import static com.bankernel.domain.LinkCobrancaTestSamples.*;
import static com.bankernel.domain.PlanoRecorrenciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssinaturaRecorrenciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssinaturaRecorrencia.class);
        AssinaturaRecorrencia assinaturaRecorrencia1 = getAssinaturaRecorrenciaSample1();
        AssinaturaRecorrencia assinaturaRecorrencia2 = new AssinaturaRecorrencia();
        assertThat(assinaturaRecorrencia1).isNotEqualTo(assinaturaRecorrencia2);

        assinaturaRecorrencia2.setId(assinaturaRecorrencia1.getId());
        assertThat(assinaturaRecorrencia1).isEqualTo(assinaturaRecorrencia2);

        assinaturaRecorrencia2 = getAssinaturaRecorrenciaSample2();
        assertThat(assinaturaRecorrencia1).isNotEqualTo(assinaturaRecorrencia2);
    }

    @Test
    void planoRecorrenciaTest() {
        AssinaturaRecorrencia assinaturaRecorrencia = getAssinaturaRecorrenciaRandomSampleGenerator();
        PlanoRecorrencia planoRecorrenciaBack = getPlanoRecorrenciaRandomSampleGenerator();

        assinaturaRecorrencia.setPlanoRecorrencia(planoRecorrenciaBack);
        assertThat(assinaturaRecorrencia.getPlanoRecorrencia()).isEqualTo(planoRecorrenciaBack);

        assinaturaRecorrencia.planoRecorrencia(null);
        assertThat(assinaturaRecorrencia.getPlanoRecorrencia()).isNull();
    }

    @Test
    void linkCobrancaTest() {
        AssinaturaRecorrencia assinaturaRecorrencia = getAssinaturaRecorrenciaRandomSampleGenerator();
        LinkCobranca linkCobrancaBack = getLinkCobrancaRandomSampleGenerator();

        assinaturaRecorrencia.setLinkCobranca(linkCobrancaBack);
        assertThat(assinaturaRecorrencia.getLinkCobranca()).isEqualTo(linkCobrancaBack);

        assinaturaRecorrencia.linkCobranca(null);
        assertThat(assinaturaRecorrencia.getLinkCobranca()).isNull();
    }
}
