package com.bankernel.domain;

import static com.bankernel.domain.LinkCobrancaTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinkCobrancaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkCobranca.class);
        LinkCobranca linkCobranca1 = getLinkCobrancaSample1();
        LinkCobranca linkCobranca2 = new LinkCobranca();
        assertThat(linkCobranca1).isNotEqualTo(linkCobranca2);

        linkCobranca2.setId(linkCobranca1.getId());
        assertThat(linkCobranca1).isEqualTo(linkCobranca2);

        linkCobranca2 = getLinkCobrancaSample2();
        assertThat(linkCobranca1).isNotEqualTo(linkCobranca2);
    }

    @Test
    void moedaCarteiraTest() {
        LinkCobranca linkCobranca = getLinkCobrancaRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        linkCobranca.setMoedaCarteira(moedaCarteiraBack);
        assertThat(linkCobranca.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        linkCobranca.moedaCarteira(null);
        assertThat(linkCobranca.getMoedaCarteira()).isNull();
    }
}
