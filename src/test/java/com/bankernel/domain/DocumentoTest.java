package com.bankernel.domain;

import static com.bankernel.domain.DocumentoTestSamples.*;
import static com.bankernel.domain.PessoaJuridicaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Documento.class);
        Documento documento1 = getDocumentoSample1();
        Documento documento2 = new Documento();
        assertThat(documento1).isNotEqualTo(documento2);

        documento2.setId(documento1.getId());
        assertThat(documento1).isEqualTo(documento2);

        documento2 = getDocumentoSample2();
        assertThat(documento1).isNotEqualTo(documento2);
    }

    @Test
    void pessoaJuridicaTest() {
        Documento documento = getDocumentoRandomSampleGenerator();
        PessoaJuridica pessoaJuridicaBack = getPessoaJuridicaRandomSampleGenerator();

        documento.setPessoaJuridica(pessoaJuridicaBack);
        assertThat(documento.getPessoaJuridica()).isEqualTo(pessoaJuridicaBack);
        assertThat(pessoaJuridicaBack.getContratoSocial()).isEqualTo(documento);

        documento.pessoaJuridica(null);
        assertThat(documento.getPessoaJuridica()).isNull();
        assertThat(pessoaJuridicaBack.getContratoSocial()).isNull();
    }
}
