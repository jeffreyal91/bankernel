package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ColaboradorPJDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ColaboradorPJDTO.class);
        ColaboradorPJDTO colaboradorPJDTO1 = new ColaboradorPJDTO();
        colaboradorPJDTO1.setId(1L);
        ColaboradorPJDTO colaboradorPJDTO2 = new ColaboradorPJDTO();
        assertThat(colaboradorPJDTO1).isNotEqualTo(colaboradorPJDTO2);
        colaboradorPJDTO2.setId(colaboradorPJDTO1.getId());
        assertThat(colaboradorPJDTO1).isEqualTo(colaboradorPJDTO2);
        colaboradorPJDTO2.setId(2L);
        assertThat(colaboradorPJDTO1).isNotEqualTo(colaboradorPJDTO2);
        colaboradorPJDTO1.setId(null);
        assertThat(colaboradorPJDTO1).isNotEqualTo(colaboradorPJDTO2);
    }
}
