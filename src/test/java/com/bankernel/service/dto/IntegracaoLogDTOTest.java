package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegracaoLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegracaoLogDTO.class);
        IntegracaoLogDTO integracaoLogDTO1 = new IntegracaoLogDTO();
        integracaoLogDTO1.setId(1L);
        IntegracaoLogDTO integracaoLogDTO2 = new IntegracaoLogDTO();
        assertThat(integracaoLogDTO1).isNotEqualTo(integracaoLogDTO2);
        integracaoLogDTO2.setId(integracaoLogDTO1.getId());
        assertThat(integracaoLogDTO1).isEqualTo(integracaoLogDTO2);
        integracaoLogDTO2.setId(2L);
        assertThat(integracaoLogDTO1).isNotEqualTo(integracaoLogDTO2);
        integracaoLogDTO1.setId(null);
        assertThat(integracaoLogDTO1).isNotEqualTo(integracaoLogDTO2);
    }
}
