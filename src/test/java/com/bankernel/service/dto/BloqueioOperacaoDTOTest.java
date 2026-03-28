package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BloqueioOperacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BloqueioOperacaoDTO.class);
        BloqueioOperacaoDTO bloqueioOperacaoDTO1 = new BloqueioOperacaoDTO();
        bloqueioOperacaoDTO1.setId(1L);
        BloqueioOperacaoDTO bloqueioOperacaoDTO2 = new BloqueioOperacaoDTO();
        assertThat(bloqueioOperacaoDTO1).isNotEqualTo(bloqueioOperacaoDTO2);
        bloqueioOperacaoDTO2.setId(bloqueioOperacaoDTO1.getId());
        assertThat(bloqueioOperacaoDTO1).isEqualTo(bloqueioOperacaoDTO2);
        bloqueioOperacaoDTO2.setId(2L);
        assertThat(bloqueioOperacaoDTO1).isNotEqualTo(bloqueioOperacaoDTO2);
        bloqueioOperacaoDTO1.setId(null);
        assertThat(bloqueioOperacaoDTO1).isNotEqualTo(bloqueioOperacaoDTO2);
    }
}
