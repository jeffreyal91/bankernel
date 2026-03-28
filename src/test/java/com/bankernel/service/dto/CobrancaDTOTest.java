package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CobrancaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CobrancaDTO.class);
        CobrancaDTO cobrancaDTO1 = new CobrancaDTO();
        cobrancaDTO1.setId(1L);
        CobrancaDTO cobrancaDTO2 = new CobrancaDTO();
        assertThat(cobrancaDTO1).isNotEqualTo(cobrancaDTO2);
        cobrancaDTO2.setId(cobrancaDTO1.getId());
        assertThat(cobrancaDTO1).isEqualTo(cobrancaDTO2);
        cobrancaDTO2.setId(2L);
        assertThat(cobrancaDTO1).isNotEqualTo(cobrancaDTO2);
        cobrancaDTO1.setId(null);
        assertThat(cobrancaDTO1).isNotEqualTo(cobrancaDTO2);
    }
}
