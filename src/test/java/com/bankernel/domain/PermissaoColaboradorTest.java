package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.ColaboradorPJTestSamples.*;
import static com.bankernel.domain.PermissaoColaboradorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermissaoColaboradorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissaoColaborador.class);
        PermissaoColaborador permissaoColaborador1 = getPermissaoColaboradorSample1();
        PermissaoColaborador permissaoColaborador2 = new PermissaoColaborador();
        assertThat(permissaoColaborador1).isNotEqualTo(permissaoColaborador2);

        permissaoColaborador2.setId(permissaoColaborador1.getId());
        assertThat(permissaoColaborador1).isEqualTo(permissaoColaborador2);

        permissaoColaborador2 = getPermissaoColaboradorSample2();
        assertThat(permissaoColaborador1).isNotEqualTo(permissaoColaborador2);
    }

    @Test
    void colaboradorTest() {
        PermissaoColaborador permissaoColaborador = getPermissaoColaboradorRandomSampleGenerator();
        ColaboradorPJ colaboradorPJBack = getColaboradorPJRandomSampleGenerator();

        permissaoColaborador.setColaborador(colaboradorPJBack);
        assertThat(permissaoColaborador.getColaborador()).isEqualTo(colaboradorPJBack);

        permissaoColaborador.colaborador(null);
        assertThat(permissaoColaborador.getColaborador()).isNull();
    }

    @Test
    void carteiraTest() {
        PermissaoColaborador permissaoColaborador = getPermissaoColaboradorRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        permissaoColaborador.setCarteira(carteiraBack);
        assertThat(permissaoColaborador.getCarteira()).isEqualTo(carteiraBack);

        permissaoColaborador.carteira(null);
        assertThat(permissaoColaborador.getCarteira()).isNull();
    }
}
