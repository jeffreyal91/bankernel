package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoOperacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoOperacaoDTO.class);
        TipoOperacaoDTO tipoOperacaoDTO1 = new TipoOperacaoDTO();
        tipoOperacaoDTO1.setId(1L);
        TipoOperacaoDTO tipoOperacaoDTO2 = new TipoOperacaoDTO();
        assertThat(tipoOperacaoDTO1).isNotEqualTo(tipoOperacaoDTO2);
        tipoOperacaoDTO2.setId(tipoOperacaoDTO1.getId());
        assertThat(tipoOperacaoDTO1).isEqualTo(tipoOperacaoDTO2);
        tipoOperacaoDTO2.setId(2L);
        assertThat(tipoOperacaoDTO1).isNotEqualTo(tipoOperacaoDTO2);
        tipoOperacaoDTO1.setId(null);
        assertThat(tipoOperacaoDTO1).isNotEqualTo(tipoOperacaoDTO2);
    }
}
