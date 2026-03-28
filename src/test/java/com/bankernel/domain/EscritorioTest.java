package com.bankernel.domain;

import static com.bankernel.domain.EscritorioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EscritorioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Escritorio.class);
        Escritorio escritorio1 = getEscritorioSample1();
        Escritorio escritorio2 = new Escritorio();
        assertThat(escritorio1).isNotEqualTo(escritorio2);

        escritorio2.setId(escritorio1.getId());
        assertThat(escritorio1).isEqualTo(escritorio2);

        escritorio2 = getEscritorioSample2();
        assertThat(escritorio1).isNotEqualTo(escritorio2);
    }
}
