package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PessoaJuridicaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PessoaJuridicaDTO.class);
        PessoaJuridicaDTO pessoaJuridicaDTO1 = new PessoaJuridicaDTO();
        pessoaJuridicaDTO1.setId(1L);
        PessoaJuridicaDTO pessoaJuridicaDTO2 = new PessoaJuridicaDTO();
        assertThat(pessoaJuridicaDTO1).isNotEqualTo(pessoaJuridicaDTO2);
        pessoaJuridicaDTO2.setId(pessoaJuridicaDTO1.getId());
        assertThat(pessoaJuridicaDTO1).isEqualTo(pessoaJuridicaDTO2);
        pessoaJuridicaDTO2.setId(2L);
        assertThat(pessoaJuridicaDTO1).isNotEqualTo(pessoaJuridicaDTO2);
        pessoaJuridicaDTO1.setId(null);
        assertThat(pessoaJuridicaDTO1).isNotEqualTo(pessoaJuridicaDTO2);
    }
}
