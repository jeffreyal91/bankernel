package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssinaturaRecorrenciaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssinaturaRecorrenciaDTO.class);
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO1 = new AssinaturaRecorrenciaDTO();
        assinaturaRecorrenciaDTO1.setId(1L);
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO2 = new AssinaturaRecorrenciaDTO();
        assertThat(assinaturaRecorrenciaDTO1).isNotEqualTo(assinaturaRecorrenciaDTO2);
        assinaturaRecorrenciaDTO2.setId(assinaturaRecorrenciaDTO1.getId());
        assertThat(assinaturaRecorrenciaDTO1).isEqualTo(assinaturaRecorrenciaDTO2);
        assinaturaRecorrenciaDTO2.setId(2L);
        assertThat(assinaturaRecorrenciaDTO1).isNotEqualTo(assinaturaRecorrenciaDTO2);
        assinaturaRecorrenciaDTO1.setId(null);
        assertThat(assinaturaRecorrenciaDTO1).isNotEqualTo(assinaturaRecorrenciaDTO2);
    }
}
