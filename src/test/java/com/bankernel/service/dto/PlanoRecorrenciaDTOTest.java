package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanoRecorrenciaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoRecorrenciaDTO.class);
        PlanoRecorrenciaDTO planoRecorrenciaDTO1 = new PlanoRecorrenciaDTO();
        planoRecorrenciaDTO1.setId(1L);
        PlanoRecorrenciaDTO planoRecorrenciaDTO2 = new PlanoRecorrenciaDTO();
        assertThat(planoRecorrenciaDTO1).isNotEqualTo(planoRecorrenciaDTO2);
        planoRecorrenciaDTO2.setId(planoRecorrenciaDTO1.getId());
        assertThat(planoRecorrenciaDTO1).isEqualTo(planoRecorrenciaDTO2);
        planoRecorrenciaDTO2.setId(2L);
        assertThat(planoRecorrenciaDTO1).isNotEqualTo(planoRecorrenciaDTO2);
        planoRecorrenciaDTO1.setId(null);
        assertThat(planoRecorrenciaDTO1).isNotEqualTo(planoRecorrenciaDTO2);
    }
}
