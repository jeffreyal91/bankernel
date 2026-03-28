package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeriadoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeriadoDTO.class);
        FeriadoDTO feriadoDTO1 = new FeriadoDTO();
        feriadoDTO1.setId(1L);
        FeriadoDTO feriadoDTO2 = new FeriadoDTO();
        assertThat(feriadoDTO1).isNotEqualTo(feriadoDTO2);
        feriadoDTO2.setId(feriadoDTO1.getId());
        assertThat(feriadoDTO1).isEqualTo(feriadoDTO2);
        feriadoDTO2.setId(2L);
        assertThat(feriadoDTO1).isNotEqualTo(feriadoDTO2);
        feriadoDTO1.setId(null);
        assertThat(feriadoDTO1).isNotEqualTo(feriadoDTO2);
    }
}
