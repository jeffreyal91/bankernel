package com.bankernel.domain;

import static com.bankernel.domain.ContaContabilTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaContabilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaContabil.class);
        ContaContabil contaContabil1 = getContaContabilSample1();
        ContaContabil contaContabil2 = new ContaContabil();
        assertThat(contaContabil1).isNotEqualTo(contaContabil2);

        contaContabil2.setId(contaContabil1.getId());
        assertThat(contaContabil1).isEqualTo(contaContabil2);

        contaContabil2 = getContaContabilSample2();
        assertThat(contaContabil1).isNotEqualTo(contaContabil2);
    }

    @Test
    void moedaCarteiraTest() {
        ContaContabil contaContabil = getContaContabilRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        contaContabil.setMoedaCarteira(moedaCarteiraBack);
        assertThat(contaContabil.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        contaContabil.moedaCarteira(null);
        assertThat(contaContabil.getMoedaCarteira()).isNull();
    }
}
