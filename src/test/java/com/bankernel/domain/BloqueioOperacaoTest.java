package com.bankernel.domain;

import static com.bankernel.domain.BloqueioOperacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BloqueioOperacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BloqueioOperacao.class);
        BloqueioOperacao bloqueioOperacao1 = getBloqueioOperacaoSample1();
        BloqueioOperacao bloqueioOperacao2 = new BloqueioOperacao();
        assertThat(bloqueioOperacao1).isNotEqualTo(bloqueioOperacao2);

        bloqueioOperacao2.setId(bloqueioOperacao1.getId());
        assertThat(bloqueioOperacao1).isEqualTo(bloqueioOperacao2);

        bloqueioOperacao2 = getBloqueioOperacaoSample2();
        assertThat(bloqueioOperacao1).isNotEqualTo(bloqueioOperacao2);
    }
}
