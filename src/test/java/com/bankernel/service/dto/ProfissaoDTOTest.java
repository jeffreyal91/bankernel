package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfissaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfissaoDTO.class);
        ProfissaoDTO profissaoDTO1 = new ProfissaoDTO();
        profissaoDTO1.setId(1L);
        ProfissaoDTO profissaoDTO2 = new ProfissaoDTO();
        assertThat(profissaoDTO1).isNotEqualTo(profissaoDTO2);
        profissaoDTO2.setId(profissaoDTO1.getId());
        assertThat(profissaoDTO1).isEqualTo(profissaoDTO2);
        profissaoDTO2.setId(2L);
        assertThat(profissaoDTO1).isNotEqualTo(profissaoDTO2);
        profissaoDTO1.setId(null);
        assertThat(profissaoDTO1).isNotEqualTo(profissaoDTO2);
    }
}
