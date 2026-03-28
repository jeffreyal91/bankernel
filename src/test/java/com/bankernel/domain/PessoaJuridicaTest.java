package com.bankernel.domain;

import static com.bankernel.domain.DocumentoTestSamples.*;
import static com.bankernel.domain.EscritorioTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.PaisTestSamples.*;
import static com.bankernel.domain.PessoaJuridicaTestSamples.*;
import static com.bankernel.domain.PlanoTestSamples.*;
import static com.bankernel.domain.TipoNegocioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PessoaJuridicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PessoaJuridica.class);
        PessoaJuridica pessoaJuridica1 = getPessoaJuridicaSample1();
        PessoaJuridica pessoaJuridica2 = new PessoaJuridica();
        assertThat(pessoaJuridica1).isNotEqualTo(pessoaJuridica2);

        pessoaJuridica2.setId(pessoaJuridica1.getId());
        assertThat(pessoaJuridica1).isEqualTo(pessoaJuridica2);

        pessoaJuridica2 = getPessoaJuridicaSample2();
        assertThat(pessoaJuridica1).isNotEqualTo(pessoaJuridica2);
    }

    @Test
    void moedaPrincipalTest() {
        PessoaJuridica pessoaJuridica = getPessoaJuridicaRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        pessoaJuridica.setMoedaPrincipal(moedaCarteiraBack);
        assertThat(pessoaJuridica.getMoedaPrincipal()).isEqualTo(moedaCarteiraBack);

        pessoaJuridica.moedaPrincipal(null);
        assertThat(pessoaJuridica.getMoedaPrincipal()).isNull();
    }

    @Test
    void contratoSocialTest() {
        PessoaJuridica pessoaJuridica = getPessoaJuridicaRandomSampleGenerator();
        Documento documentoBack = getDocumentoRandomSampleGenerator();

        pessoaJuridica.setContratoSocial(documentoBack);
        assertThat(pessoaJuridica.getContratoSocial()).isEqualTo(documentoBack);

        pessoaJuridica.contratoSocial(null);
        assertThat(pessoaJuridica.getContratoSocial()).isNull();
    }

    @Test
    void nacionalidadeTest() {
        PessoaJuridica pessoaJuridica = getPessoaJuridicaRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        pessoaJuridica.setNacionalidade(paisBack);
        assertThat(pessoaJuridica.getNacionalidade()).isEqualTo(paisBack);

        pessoaJuridica.nacionalidade(null);
        assertThat(pessoaJuridica.getNacionalidade()).isNull();
    }

    @Test
    void tipoNegocioTest() {
        PessoaJuridica pessoaJuridica = getPessoaJuridicaRandomSampleGenerator();
        TipoNegocio tipoNegocioBack = getTipoNegocioRandomSampleGenerator();

        pessoaJuridica.setTipoNegocio(tipoNegocioBack);
        assertThat(pessoaJuridica.getTipoNegocio()).isEqualTo(tipoNegocioBack);

        pessoaJuridica.tipoNegocio(null);
        assertThat(pessoaJuridica.getTipoNegocio()).isNull();
    }

    @Test
    void planoTest() {
        PessoaJuridica pessoaJuridica = getPessoaJuridicaRandomSampleGenerator();
        Plano planoBack = getPlanoRandomSampleGenerator();

        pessoaJuridica.setPlano(planoBack);
        assertThat(pessoaJuridica.getPlano()).isEqualTo(planoBack);

        pessoaJuridica.plano(null);
        assertThat(pessoaJuridica.getPlano()).isNull();
    }

    @Test
    void escritorioTest() {
        PessoaJuridica pessoaJuridica = getPessoaJuridicaRandomSampleGenerator();
        Escritorio escritorioBack = getEscritorioRandomSampleGenerator();

        pessoaJuridica.setEscritorio(escritorioBack);
        assertThat(pessoaJuridica.getEscritorio()).isEqualTo(escritorioBack);

        pessoaJuridica.escritorio(null);
        assertThat(pessoaJuridica.getEscritorio()).isNull();
    }
}
