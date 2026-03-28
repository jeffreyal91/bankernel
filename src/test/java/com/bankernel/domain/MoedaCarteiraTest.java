package com.bankernel.domain;

import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.MoedaTestSamples.*;
import static com.bankernel.domain.PessoaFisicaTestSamples.*;
import static com.bankernel.domain.PessoaJuridicaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoedaCarteiraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoedaCarteira.class);
        MoedaCarteira moedaCarteira1 = getMoedaCarteiraSample1();
        MoedaCarteira moedaCarteira2 = new MoedaCarteira();
        assertThat(moedaCarteira1).isNotEqualTo(moedaCarteira2);

        moedaCarteira2.setId(moedaCarteira1.getId());
        assertThat(moedaCarteira1).isEqualTo(moedaCarteira2);

        moedaCarteira2 = getMoedaCarteiraSample2();
        assertThat(moedaCarteira1).isNotEqualTo(moedaCarteira2);
    }

    @Test
    void moedaTest() {
        MoedaCarteira moedaCarteira = getMoedaCarteiraRandomSampleGenerator();
        Moeda moedaBack = getMoedaRandomSampleGenerator();

        moedaCarteira.setMoeda(moedaBack);
        assertThat(moedaCarteira.getMoeda()).isEqualTo(moedaBack);

        moedaCarteira.moeda(null);
        assertThat(moedaCarteira.getMoeda()).isNull();
    }

    @Test
    void pessoaFisicaTest() {
        MoedaCarteira moedaCarteira = getMoedaCarteiraRandomSampleGenerator();
        PessoaFisica pessoaFisicaBack = getPessoaFisicaRandomSampleGenerator();

        moedaCarteira.setPessoaFisica(pessoaFisicaBack);
        assertThat(moedaCarteira.getPessoaFisica()).isEqualTo(pessoaFisicaBack);
        assertThat(pessoaFisicaBack.getMoedaPrincipal()).isEqualTo(moedaCarteira);

        moedaCarteira.pessoaFisica(null);
        assertThat(moedaCarteira.getPessoaFisica()).isNull();
        assertThat(pessoaFisicaBack.getMoedaPrincipal()).isNull();
    }

    @Test
    void pessoaJuridicaTest() {
        MoedaCarteira moedaCarteira = getMoedaCarteiraRandomSampleGenerator();
        PessoaJuridica pessoaJuridicaBack = getPessoaJuridicaRandomSampleGenerator();

        moedaCarteira.setPessoaJuridica(pessoaJuridicaBack);
        assertThat(moedaCarteira.getPessoaJuridica()).isEqualTo(pessoaJuridicaBack);
        assertThat(pessoaJuridicaBack.getMoedaPrincipal()).isEqualTo(moedaCarteira);

        moedaCarteira.pessoaJuridica(null);
        assertThat(moedaCarteira.getPessoaJuridica()).isNull();
        assertThat(pessoaJuridicaBack.getMoedaPrincipal()).isNull();
    }
}
