package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfiguracaoSistemaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfiguracaoSistemaDTO.class);
        ConfiguracaoSistemaDTO configuracaoSistemaDTO1 = new ConfiguracaoSistemaDTO();
        configuracaoSistemaDTO1.setId(1L);
        ConfiguracaoSistemaDTO configuracaoSistemaDTO2 = new ConfiguracaoSistemaDTO();
        assertThat(configuracaoSistemaDTO1).isNotEqualTo(configuracaoSistemaDTO2);
        configuracaoSistemaDTO2.setId(configuracaoSistemaDTO1.getId());
        assertThat(configuracaoSistemaDTO1).isEqualTo(configuracaoSistemaDTO2);
        configuracaoSistemaDTO2.setId(2L);
        assertThat(configuracaoSistemaDTO1).isNotEqualTo(configuracaoSistemaDTO2);
        configuracaoSistemaDTO1.setId(null);
        assertThat(configuracaoSistemaDTO1).isNotEqualTo(configuracaoSistemaDTO2);
    }
}
