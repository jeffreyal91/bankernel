package com.bankernel.domain;

import static com.bankernel.domain.AdministradorTestSamples.*;
import static com.bankernel.domain.EscritorioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdministradorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Administrador.class);
        Administrador administrador1 = getAdministradorSample1();
        Administrador administrador2 = new Administrador();
        assertThat(administrador1).isNotEqualTo(administrador2);

        administrador2.setId(administrador1.getId());
        assertThat(administrador1).isEqualTo(administrador2);

        administrador2 = getAdministradorSample2();
        assertThat(administrador1).isNotEqualTo(administrador2);
    }

    @Test
    void escritorioTest() {
        Administrador administrador = getAdministradorRandomSampleGenerator();
        Escritorio escritorioBack = getEscritorioRandomSampleGenerator();

        administrador.setEscritorio(escritorioBack);
        assertThat(administrador.getEscritorio()).isEqualTo(escritorioBack);

        administrador.escritorio(null);
        assertThat(administrador.getEscritorio()).isNull();
    }
}
