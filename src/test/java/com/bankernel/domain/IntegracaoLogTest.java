package com.bankernel.domain;

import static com.bankernel.domain.IntegracaoLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegracaoLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegracaoLog.class);
        IntegracaoLog integracaoLog1 = getIntegracaoLogSample1();
        IntegracaoLog integracaoLog2 = new IntegracaoLog();
        assertThat(integracaoLog1).isNotEqualTo(integracaoLog2);

        integracaoLog2.setId(integracaoLog1.getId());
        assertThat(integracaoLog1).isEqualTo(integracaoLog2);

        integracaoLog2 = getIntegracaoLogSample2();
        assertThat(integracaoLog1).isNotEqualTo(integracaoLog2);
    }
}
