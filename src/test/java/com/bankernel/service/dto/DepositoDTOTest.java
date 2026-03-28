package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepositoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositoDTO.class);
        DepositoDTO depositoDTO1 = new DepositoDTO();
        depositoDTO1.setId(1L);
        DepositoDTO depositoDTO2 = new DepositoDTO();
        assertThat(depositoDTO1).isNotEqualTo(depositoDTO2);
        depositoDTO2.setId(depositoDTO1.getId());
        assertThat(depositoDTO1).isEqualTo(depositoDTO2);
        depositoDTO2.setId(2L);
        assertThat(depositoDTO1).isNotEqualTo(depositoDTO2);
        depositoDTO1.setId(null);
        assertThat(depositoDTO1).isNotEqualTo(depositoDTO2);
    }
}
