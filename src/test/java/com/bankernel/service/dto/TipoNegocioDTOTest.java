package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoNegocioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoNegocioDTO.class);
        TipoNegocioDTO tipoNegocioDTO1 = new TipoNegocioDTO();
        tipoNegocioDTO1.setId(1L);
        TipoNegocioDTO tipoNegocioDTO2 = new TipoNegocioDTO();
        assertThat(tipoNegocioDTO1).isNotEqualTo(tipoNegocioDTO2);
        tipoNegocioDTO2.setId(tipoNegocioDTO1.getId());
        assertThat(tipoNegocioDTO1).isEqualTo(tipoNegocioDTO2);
        tipoNegocioDTO2.setId(2L);
        assertThat(tipoNegocioDTO1).isNotEqualTo(tipoNegocioDTO2);
        tipoNegocioDTO1.setId(null);
        assertThat(tipoNegocioDTO1).isNotEqualTo(tipoNegocioDTO2);
    }
}
