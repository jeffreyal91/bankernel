package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaqueBoletoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaqueBoletoDTO.class);
        SaqueBoletoDTO saqueBoletoDTO1 = new SaqueBoletoDTO();
        saqueBoletoDTO1.setId(1L);
        SaqueBoletoDTO saqueBoletoDTO2 = new SaqueBoletoDTO();
        assertThat(saqueBoletoDTO1).isNotEqualTo(saqueBoletoDTO2);
        saqueBoletoDTO2.setId(saqueBoletoDTO1.getId());
        assertThat(saqueBoletoDTO1).isEqualTo(saqueBoletoDTO2);
        saqueBoletoDTO2.setId(2L);
        assertThat(saqueBoletoDTO1).isNotEqualTo(saqueBoletoDTO2);
        saqueBoletoDTO1.setId(null);
        assertThat(saqueBoletoDTO1).isNotEqualTo(saqueBoletoDTO2);
    }
}
