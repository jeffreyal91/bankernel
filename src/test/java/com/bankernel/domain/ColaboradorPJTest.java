package com.bankernel.domain;

import static com.bankernel.domain.ColaboradorPJTestSamples.*;
import static com.bankernel.domain.PessoaJuridicaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ColaboradorPJTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ColaboradorPJ.class);
        ColaboradorPJ colaboradorPJ1 = getColaboradorPJSample1();
        ColaboradorPJ colaboradorPJ2 = new ColaboradorPJ();
        assertThat(colaboradorPJ1).isNotEqualTo(colaboradorPJ2);

        colaboradorPJ2.setId(colaboradorPJ1.getId());
        assertThat(colaboradorPJ1).isEqualTo(colaboradorPJ2);

        colaboradorPJ2 = getColaboradorPJSample2();
        assertThat(colaboradorPJ1).isNotEqualTo(colaboradorPJ2);
    }

    @Test
    void pessoaJuridicaTest() {
        ColaboradorPJ colaboradorPJ = getColaboradorPJRandomSampleGenerator();
        PessoaJuridica pessoaJuridicaBack = getPessoaJuridicaRandomSampleGenerator();

        colaboradorPJ.setPessoaJuridica(pessoaJuridicaBack);
        assertThat(colaboradorPJ.getPessoaJuridica()).isEqualTo(pessoaJuridicaBack);

        colaboradorPJ.pessoaJuridica(null);
        assertThat(colaboradorPJ.getPessoaJuridica()).isNull();
    }
}
