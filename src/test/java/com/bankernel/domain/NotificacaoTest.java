package com.bankernel.domain;

import static com.bankernel.domain.NotificacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notificacao.class);
        Notificacao notificacao1 = getNotificacaoSample1();
        Notificacao notificacao2 = new Notificacao();
        assertThat(notificacao1).isNotEqualTo(notificacao2);

        notificacao2.setId(notificacao1.getId());
        assertThat(notificacao1).isEqualTo(notificacao2);

        notificacao2 = getNotificacaoSample2();
        assertThat(notificacao1).isNotEqualTo(notificacao2);
    }
}
