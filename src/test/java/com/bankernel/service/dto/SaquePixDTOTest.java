package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaquePixDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaquePixDTO.class);
        SaquePixDTO saquePixDTO1 = new SaquePixDTO();
        saquePixDTO1.setId(1L);
        SaquePixDTO saquePixDTO2 = new SaquePixDTO();
        assertThat(saquePixDTO1).isNotEqualTo(saquePixDTO2);
        saquePixDTO2.setId(saquePixDTO1.getId());
        assertThat(saquePixDTO1).isEqualTo(saquePixDTO2);
        saquePixDTO2.setId(2L);
        assertThat(saquePixDTO1).isNotEqualTo(saquePixDTO2);
        saquePixDTO1.setId(null);
        assertThat(saquePixDTO1).isNotEqualTo(saquePixDTO2);
    }
}
