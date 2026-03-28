package com.bankernel.domain;

import static com.bankernel.domain.MoedaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoedaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Moeda.class);
        Moeda moeda1 = getMoedaSample1();
        Moeda moeda2 = new Moeda();
        assertThat(moeda1).isNotEqualTo(moeda2);

        moeda2.setId(moeda1.getId());
        assertThat(moeda1).isEqualTo(moeda2);

        moeda2 = getMoedaSample2();
        assertThat(moeda1).isNotEqualTo(moeda2);
    }
}
