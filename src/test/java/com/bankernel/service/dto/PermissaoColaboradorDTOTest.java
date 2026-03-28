package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermissaoColaboradorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissaoColaboradorDTO.class);
        PermissaoColaboradorDTO permissaoColaboradorDTO1 = new PermissaoColaboradorDTO();
        permissaoColaboradorDTO1.setId(1L);
        PermissaoColaboradorDTO permissaoColaboradorDTO2 = new PermissaoColaboradorDTO();
        assertThat(permissaoColaboradorDTO1).isNotEqualTo(permissaoColaboradorDTO2);
        permissaoColaboradorDTO2.setId(permissaoColaboradorDTO1.getId());
        assertThat(permissaoColaboradorDTO1).isEqualTo(permissaoColaboradorDTO2);
        permissaoColaboradorDTO2.setId(2L);
        assertThat(permissaoColaboradorDTO1).isNotEqualTo(permissaoColaboradorDTO2);
        permissaoColaboradorDTO1.setId(null);
        assertThat(permissaoColaboradorDTO1).isNotEqualTo(permissaoColaboradorDTO2);
    }
}
