package com.bankernel.domain;

import static com.bankernel.domain.ConfiguracaoSistemaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfiguracaoSistemaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfiguracaoSistema.class);
        ConfiguracaoSistema configuracaoSistema1 = getConfiguracaoSistemaSample1();
        ConfiguracaoSistema configuracaoSistema2 = new ConfiguracaoSistema();
        assertThat(configuracaoSistema1).isNotEqualTo(configuracaoSistema2);

        configuracaoSistema2.setId(configuracaoSistema1.getId());
        assertThat(configuracaoSistema1).isEqualTo(configuracaoSistema2);

        configuracaoSistema2 = getConfiguracaoSistemaSample2();
        assertThat(configuracaoSistema1).isNotEqualTo(configuracaoSistema2);
    }
}
