package com.bankernel.domain;

import static com.bankernel.domain.BancoReferenciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BancoReferenciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BancoReferencia.class);
        BancoReferencia bancoReferencia1 = getBancoReferenciaSample1();
        BancoReferencia bancoReferencia2 = new BancoReferencia();
        assertThat(bancoReferencia1).isNotEqualTo(bancoReferencia2);

        bancoReferencia2.setId(bancoReferencia1.getId());
        assertThat(bancoReferencia1).isEqualTo(bancoReferencia2);

        bancoReferencia2 = getBancoReferenciaSample2();
        assertThat(bancoReferencia1).isNotEqualTo(bancoReferencia2);
    }
}
