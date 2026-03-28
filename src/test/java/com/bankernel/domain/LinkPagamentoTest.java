package com.bankernel.domain;

import static com.bankernel.domain.LinkPagamentoTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinkPagamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkPagamento.class);
        LinkPagamento linkPagamento1 = getLinkPagamentoSample1();
        LinkPagamento linkPagamento2 = new LinkPagamento();
        assertThat(linkPagamento1).isNotEqualTo(linkPagamento2);

        linkPagamento2.setId(linkPagamento1.getId());
        assertThat(linkPagamento1).isEqualTo(linkPagamento2);

        linkPagamento2 = getLinkPagamentoSample2();
        assertThat(linkPagamento1).isNotEqualTo(linkPagamento2);
    }

    @Test
    void moedaCarteiraTest() {
        LinkPagamento linkPagamento = getLinkPagamentoRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        linkPagamento.setMoedaCarteira(moedaCarteiraBack);
        assertThat(linkPagamento.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        linkPagamento.moedaCarteira(null);
        assertThat(linkPagamento.getMoedaCarteira()).isNull();
    }
}
