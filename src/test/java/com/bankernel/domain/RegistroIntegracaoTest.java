package com.bankernel.domain;

import static com.bankernel.domain.RegistroIntegracaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistroIntegracaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroIntegracao.class);
        RegistroIntegracao registroIntegracao1 = getRegistroIntegracaoSample1();
        RegistroIntegracao registroIntegracao2 = new RegistroIntegracao();
        assertThat(registroIntegracao1).isNotEqualTo(registroIntegracao2);

        registroIntegracao2.setId(registroIntegracao1.getId());
        assertThat(registroIntegracao1).isEqualTo(registroIntegracao2);

        registroIntegracao2 = getRegistroIntegracaoSample2();
        assertThat(registroIntegracao1).isNotEqualTo(registroIntegracao2);
    }
}
