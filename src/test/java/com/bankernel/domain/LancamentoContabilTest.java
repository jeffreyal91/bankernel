package com.bankernel.domain;

import static com.bankernel.domain.ContaContabilTestSamples.*;
import static com.bankernel.domain.LancamentoContabilTestSamples.*;
import static com.bankernel.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LancamentoContabilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LancamentoContabil.class);
        LancamentoContabil lancamentoContabil1 = getLancamentoContabilSample1();
        LancamentoContabil lancamentoContabil2 = new LancamentoContabil();
        assertThat(lancamentoContabil1).isNotEqualTo(lancamentoContabil2);

        lancamentoContabil2.setId(lancamentoContabil1.getId());
        assertThat(lancamentoContabil1).isEqualTo(lancamentoContabil2);

        lancamentoContabil2 = getLancamentoContabilSample2();
        assertThat(lancamentoContabil1).isNotEqualTo(lancamentoContabil2);
    }

    @Test
    void transacaoTest() {
        LancamentoContabil lancamentoContabil = getLancamentoContabilRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        lancamentoContabil.setTransacao(transacaoBack);
        assertThat(lancamentoContabil.getTransacao()).isEqualTo(transacaoBack);

        lancamentoContabil.transacao(null);
        assertThat(lancamentoContabil.getTransacao()).isNull();
    }

    @Test
    void contaContabilTest() {
        LancamentoContabil lancamentoContabil = getLancamentoContabilRandomSampleGenerator();
        ContaContabil contaContabilBack = getContaContabilRandomSampleGenerator();

        lancamentoContabil.setContaContabil(contaContabilBack);
        assertThat(lancamentoContabil.getContaContabil()).isEqualTo(contaContabilBack);

        lancamentoContabil.contaContabil(null);
        assertThat(lancamentoContabil.getContaContabil()).isNull();
    }
}
