package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaqueDTO.class);
        SaqueDTO saqueDTO1 = new SaqueDTO();
        saqueDTO1.setId(1L);
        SaqueDTO saqueDTO2 = new SaqueDTO();
        assertThat(saqueDTO1).isNotEqualTo(saqueDTO2);
        saqueDTO2.setId(saqueDTO1.getId());
        assertThat(saqueDTO1).isEqualTo(saqueDTO2);
        saqueDTO2.setId(2L);
        assertThat(saqueDTO1).isNotEqualTo(saqueDTO2);
        saqueDTO1.setId(null);
        assertThat(saqueDTO1).isNotEqualTo(saqueDTO2);
    }
}
