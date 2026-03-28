package com.bankernel.domain;

import static com.bankernel.domain.TipoNegocioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoNegocioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoNegocio.class);
        TipoNegocio tipoNegocio1 = getTipoNegocioSample1();
        TipoNegocio tipoNegocio2 = new TipoNegocio();
        assertThat(tipoNegocio1).isNotEqualTo(tipoNegocio2);

        tipoNegocio2.setId(tipoNegocio1.getId());
        assertThat(tipoNegocio1).isEqualTo(tipoNegocio2);

        tipoNegocio2 = getTipoNegocioSample2();
        assertThat(tipoNegocio1).isNotEqualTo(tipoNegocio2);
    }
}
