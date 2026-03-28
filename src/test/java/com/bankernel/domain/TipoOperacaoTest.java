package com.bankernel.domain;

import static com.bankernel.domain.ContaContabilTestSamples.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;
import static com.bankernel.domain.TipoOperacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoOperacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoOperacao.class);
        TipoOperacao tipoOperacao1 = getTipoOperacaoSample1();
        TipoOperacao tipoOperacao2 = new TipoOperacao();
        assertThat(tipoOperacao1).isNotEqualTo(tipoOperacao2);

        tipoOperacao2.setId(tipoOperacao1.getId());
        assertThat(tipoOperacao1).isEqualTo(tipoOperacao2);

        tipoOperacao2 = getTipoOperacaoSample2();
        assertThat(tipoOperacao1).isNotEqualTo(tipoOperacao2);
    }

    @Test
    void contaCreditoTest() {
        TipoOperacao tipoOperacao = getTipoOperacaoRandomSampleGenerator();
        ContaContabil contaContabilBack = getContaContabilRandomSampleGenerator();

        tipoOperacao.setContaCredito(contaContabilBack);
        assertThat(tipoOperacao.getContaCredito()).isEqualTo(contaContabilBack);

        tipoOperacao.contaCredito(null);
        assertThat(tipoOperacao.getContaCredito()).isNull();
    }

    @Test
    void contaDebitoTest() {
        TipoOperacao tipoOperacao = getTipoOperacaoRandomSampleGenerator();
        ContaContabil contaContabilBack = getContaContabilRandomSampleGenerator();

        tipoOperacao.setContaDebito(contaContabilBack);
        assertThat(tipoOperacao.getContaDebito()).isEqualTo(contaContabilBack);

        tipoOperacao.contaDebito(null);
        assertThat(tipoOperacao.getContaDebito()).isNull();
    }

    @Test
    void moedaCarteiraTest() {
        TipoOperacao tipoOperacao = getTipoOperacaoRandomSampleGenerator();
        MoedaCarteira moedaCarteiraBack = getMoedaCarteiraRandomSampleGenerator();

        tipoOperacao.setMoedaCarteira(moedaCarteiraBack);
        assertThat(tipoOperacao.getMoedaCarteira()).isEqualTo(moedaCarteiraBack);

        tipoOperacao.moedaCarteira(null);
        assertThat(tipoOperacao.getMoedaCarteira()).isNull();
    }
}
