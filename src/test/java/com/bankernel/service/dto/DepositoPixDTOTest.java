package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepositoPixDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositoPixDTO.class);
        DepositoPixDTO depositoPixDTO1 = new DepositoPixDTO();
        depositoPixDTO1.setId(1L);
        DepositoPixDTO depositoPixDTO2 = new DepositoPixDTO();
        assertThat(depositoPixDTO1).isNotEqualTo(depositoPixDTO2);
        depositoPixDTO2.setId(depositoPixDTO1.getId());
        assertThat(depositoPixDTO1).isEqualTo(depositoPixDTO2);
        depositoPixDTO2.setId(2L);
        assertThat(depositoPixDTO1).isNotEqualTo(depositoPixDTO2);
        depositoPixDTO1.setId(null);
        assertThat(depositoPixDTO1).isNotEqualTo(depositoPixDTO2);
    }
}
