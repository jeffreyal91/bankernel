package com.bankernel.domain;

import static com.bankernel.domain.EscritorioTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.PaisTestSamples.*;
import static com.bankernel.domain.PessoaFisicaTestSamples.*;
import static com.bankernel.domain.PlanoTestSamples.*;
import static com.bankernel.domain.ProfissaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PessoaFisicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PessoaFisica.class);
        PessoaFisica pessoaFisica1 = getPessoaFisicaSample1();
        PessoaFisica pessoaFisica2 = new PessoaFisica();
        assertThat(pessoaFisica1).isNotEqualTo(pessoaFisica2);

        pessoaFisica2.setId(pessoaFisica1.getId());
        assertThat(pessoaFisica1).isEqualTo(pessoaFisica2);

        pessoaFisica2 = getPessoaFisicaSample2();
        assertThat(pessoaFisica1).isNotEqualTo(pessoaFisica2);
    }

    @Test
    void moedaPrincipalTest() {
        PessoaFisica pessoaFisica = getPessoaFisicaRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        pessoaFisica.setMoedaPrincipal(moedaCarteiraBack);
        assertThat(pessoaFisica.getMoedaPrincipal()).isEqualTo(moedaCarteiraBack);

        pessoaFisica.moedaPrincipal(null);
        assertThat(pessoaFisica.getMoedaPrincipal()).isNull();
    }

    @Test
    void nacionalidadeTest() {
        PessoaFisica pessoaFisica = getPessoaFisicaRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        pessoaFisica.setNacionalidade(paisBack);
        assertThat(pessoaFisica.getNacionalidade()).isEqualTo(paisBack);

        pessoaFisica.nacionalidade(null);
        assertThat(pessoaFisica.getNacionalidade()).isNull();
    }

    @Test
    void profissaoTest() {
        PessoaFisica pessoaFisica = getPessoaFisicaRandomSampleGenerator();
        Profissao profissaoBack = getProfissaoRandomSampleGenerator();

        pessoaFisica.setProfissao(profissaoBack);
        assertThat(pessoaFisica.getProfissao()).isEqualTo(profissaoBack);

        pessoaFisica.profissao(null);
        assertThat(pessoaFisica.getProfissao()).isNull();
    }

    @Test
    void planoTest() {
        PessoaFisica pessoaFisica = getPessoaFisicaRandomSampleGenerator();
        Plano planoBack = getPlanoRandomSampleGenerator();

        pessoaFisica.setPlano(planoBack);
        assertThat(pessoaFisica.getPlano()).isEqualTo(planoBack);

        pessoaFisica.plano(null);
        assertThat(pessoaFisica.getPlano()).isNull();
    }

    @Test
    void escritorioTest() {
        PessoaFisica pessoaFisica = getPessoaFisicaRandomSampleGenerator();
        Escritorio escritorioBack = getEscritorioRandomSampleGenerator();

        pessoaFisica.setEscritorio(escritorioBack);
        assertThat(pessoaFisica.getEscritorio()).isEqualTo(escritorioBack);

        pessoaFisica.escritorio(null);
        assertThat(pessoaFisica.getEscritorio()).isNull();
    }
}
