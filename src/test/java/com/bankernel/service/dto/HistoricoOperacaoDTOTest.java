package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricoOperacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricoOperacaoDTO.class);
        HistoricoOperacaoDTO historicoOperacaoDTO1 = new HistoricoOperacaoDTO();
        historicoOperacaoDTO1.setId(1L);
        HistoricoOperacaoDTO historicoOperacaoDTO2 = new HistoricoOperacaoDTO();
        assertThat(historicoOperacaoDTO1).isNotEqualTo(historicoOperacaoDTO2);
        historicoOperacaoDTO2.setId(historicoOperacaoDTO1.getId());
        assertThat(historicoOperacaoDTO1).isEqualTo(historicoOperacaoDTO2);
        historicoOperacaoDTO2.setId(2L);
        assertThat(historicoOperacaoDTO1).isNotEqualTo(historicoOperacaoDTO2);
        historicoOperacaoDTO1.setId(null);
        assertThat(historicoOperacaoDTO1).isNotEqualTo(historicoOperacaoDTO2);
    }
}
