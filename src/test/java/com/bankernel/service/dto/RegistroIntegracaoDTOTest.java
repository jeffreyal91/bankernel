package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistroIntegracaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroIntegracaoDTO.class);
        RegistroIntegracaoDTO registroIntegracaoDTO1 = new RegistroIntegracaoDTO();
        registroIntegracaoDTO1.setId(1L);
        RegistroIntegracaoDTO registroIntegracaoDTO2 = new RegistroIntegracaoDTO();
        assertThat(registroIntegracaoDTO1).isNotEqualTo(registroIntegracaoDTO2);
        registroIntegracaoDTO2.setId(registroIntegracaoDTO1.getId());
        assertThat(registroIntegracaoDTO1).isEqualTo(registroIntegracaoDTO2);
        registroIntegracaoDTO2.setId(2L);
        assertThat(registroIntegracaoDTO1).isNotEqualTo(registroIntegracaoDTO2);
        registroIntegracaoDTO1.setId(null);
        assertThat(registroIntegracaoDTO1).isNotEqualTo(registroIntegracaoDTO2);
    }
}
