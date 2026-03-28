package com.bankernel.domain;

import static com.bankernel.domain.CarteiraTestSamples.*;
import static com.bankernel.domain.HistoricoOperacaoTestSamples.*;
import static com.bankernel.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricoOperacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricoOperacao.class);
        HistoricoOperacao historicoOperacao1 = getHistoricoOperacaoSample1();
        HistoricoOperacao historicoOperacao2 = new HistoricoOperacao();
        assertThat(historicoOperacao1).isNotEqualTo(historicoOperacao2);

        historicoOperacao2.setId(historicoOperacao1.getId());
        assertThat(historicoOperacao1).isEqualTo(historicoOperacao2);

        historicoOperacao2 = getHistoricoOperacaoSample2();
        assertThat(historicoOperacao1).isNotEqualTo(historicoOperacao2);
    }

    @Test
    void transacaoTest() {
        HistoricoOperacao historicoOperacao = getHistoricoOperacaoRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        historicoOperacao.setTransacao(transacaoBack);
        assertThat(historicoOperacao.getTransacao()).isEqualTo(transacaoBack);

        historicoOperacao.transacao(null);
        assertThat(historicoOperacao.getTransacao()).isNull();
    }

    @Test
    void carteiraTest() {
        HistoricoOperacao historicoOperacao = getHistoricoOperacaoRandomSampleGenerator();
        Carteira carteiraBack = getCarteiraRandomSampleGenerator();

        historicoOperacao.setCarteira(carteiraBack);
        assertThat(historicoOperacao.getCarteira()).isEqualTo(carteiraBack);

        historicoOperacao.carteira(null);
        assertThat(historicoOperacao.getCarteira()).isNull();
    }
}
