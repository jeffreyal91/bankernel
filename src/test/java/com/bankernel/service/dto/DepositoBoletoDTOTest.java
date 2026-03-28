package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepositoBoletoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositoBoletoDTO.class);
        DepositoBoletoDTO depositoBoletoDTO1 = new DepositoBoletoDTO();
        depositoBoletoDTO1.setId(1L);
        DepositoBoletoDTO depositoBoletoDTO2 = new DepositoBoletoDTO();
        assertThat(depositoBoletoDTO1).isNotEqualTo(depositoBoletoDTO2);
        depositoBoletoDTO2.setId(depositoBoletoDTO1.getId());
        assertThat(depositoBoletoDTO1).isEqualTo(depositoBoletoDTO2);
        depositoBoletoDTO2.setId(2L);
        assertThat(depositoBoletoDTO1).isNotEqualTo(depositoBoletoDTO2);
        depositoBoletoDTO1.setId(null);
        assertThat(depositoBoletoDTO1).isNotEqualTo(depositoBoletoDTO2);
    }
}
