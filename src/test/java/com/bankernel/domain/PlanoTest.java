package com.bankernel.domain;

import static com.bankernel.domain.PlanoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plano.class);
        Plano plano1 = getPlanoSample1();
        Plano plano2 = new Plano();
        assertThat(plano1).isNotEqualTo(plano2);

        plano2.setId(plano1.getId());
        assertThat(plano1).isEqualTo(plano2);

        plano2 = getPlanoSample2();
        assertThat(plano1).isNotEqualTo(plano2);
    }
}
