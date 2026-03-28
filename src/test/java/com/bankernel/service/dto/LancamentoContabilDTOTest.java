package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LancamentoContabilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LancamentoContabilDTO.class);
        LancamentoContabilDTO lancamentoContabilDTO1 = new LancamentoContabilDTO();
        lancamentoContabilDTO1.setId(1L);
        LancamentoContabilDTO lancamentoContabilDTO2 = new LancamentoContabilDTO();
        assertThat(lancamentoContabilDTO1).isNotEqualTo(lancamentoContabilDTO2);
        lancamentoContabilDTO2.setId(lancamentoContabilDTO1.getId());
        assertThat(lancamentoContabilDTO1).isEqualTo(lancamentoContabilDTO2);
        lancamentoContabilDTO2.setId(2L);
        assertThat(lancamentoContabilDTO1).isNotEqualTo(lancamentoContabilDTO2);
        lancamentoContabilDTO1.setId(null);
        assertThat(lancamentoContabilDTO1).isNotEqualTo(lancamentoContabilDTO2);
    }
}
