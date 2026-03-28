package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PessoaFisicaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PessoaFisicaDTO.class);
        PessoaFisicaDTO pessoaFisicaDTO1 = new PessoaFisicaDTO();
        pessoaFisicaDTO1.setId(1L);
        PessoaFisicaDTO pessoaFisicaDTO2 = new PessoaFisicaDTO();
        assertThat(pessoaFisicaDTO1).isNotEqualTo(pessoaFisicaDTO2);
        pessoaFisicaDTO2.setId(pessoaFisicaDTO1.getId());
        assertThat(pessoaFisicaDTO1).isEqualTo(pessoaFisicaDTO2);
        pessoaFisicaDTO2.setId(2L);
        assertThat(pessoaFisicaDTO1).isNotEqualTo(pessoaFisicaDTO2);
        pessoaFisicaDTO1.setId(null);
        assertThat(pessoaFisicaDTO1).isNotEqualTo(pessoaFisicaDTO2);
    }
}
