package com.bankernel.domain;

import static com.bankernel.domain.FeriadoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeriadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feriado.class);
        Feriado feriado1 = getFeriadoSample1();
        Feriado feriado2 = new Feriado();
        assertThat(feriado1).isNotEqualTo(feriado2);

        feriado2.setId(feriado1.getId());
        assertThat(feriado1).isEqualTo(feriado2);

        feriado2 = getFeriadoSample2();
        assertThat(feriado1).isNotEqualTo(feriado2);
    }
}
