package com.bankernel.domain;

import static com.bankernel.domain.SaquePixTestSamples.*;
import static com.bankernel.domain.SaqueTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaquePixTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaquePix.class);
        SaquePix saquePix1 = getSaquePixSample1();
        SaquePix saquePix2 = new SaquePix();
        assertThat(saquePix1).isNotEqualTo(saquePix2);

        saquePix2.setId(saquePix1.getId());
        assertThat(saquePix1).isEqualTo(saquePix2);

        saquePix2 = getSaquePixSample2();
        assertThat(saquePix1).isNotEqualTo(saquePix2);
    }

    @Test
    void saqueTest() {
        SaquePix saquePix = getSaquePixRandomSampleGenerator();
        Saque saqueBack = getSaqueRandomSampleGenerator();

        saquePix.setSaque(saqueBack);
        assertThat(saquePix.getSaque()).isEqualTo(saqueBack);

        saquePix.saque(null);
        assertThat(saquePix.getSaque()).isNull();
    }
}
