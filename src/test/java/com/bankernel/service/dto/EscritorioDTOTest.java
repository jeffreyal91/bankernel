package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EscritorioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EscritorioDTO.class);
        EscritorioDTO escritorioDTO1 = new EscritorioDTO();
        escritorioDTO1.setId(1L);
        EscritorioDTO escritorioDTO2 = new EscritorioDTO();
        assertThat(escritorioDTO1).isNotEqualTo(escritorioDTO2);
        escritorioDTO2.setId(escritorioDTO1.getId());
        assertThat(escritorioDTO1).isEqualTo(escritorioDTO2);
        escritorioDTO2.setId(2L);
        assertThat(escritorioDTO1).isNotEqualTo(escritorioDTO2);
        escritorioDTO1.setId(null);
        assertThat(escritorioDTO1).isNotEqualTo(escritorioDTO2);
    }
}
