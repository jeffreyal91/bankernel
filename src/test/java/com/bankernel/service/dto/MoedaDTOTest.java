package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoedaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoedaDTO.class);
        MoedaDTO moedaDTO1 = new MoedaDTO();
        moedaDTO1.setId(1L);
        MoedaDTO moedaDTO2 = new MoedaDTO();
        assertThat(moedaDTO1).isNotEqualTo(moedaDTO2);
        moedaDTO2.setId(moedaDTO1.getId());
        assertThat(moedaDTO1).isEqualTo(moedaDTO2);
        moedaDTO2.setId(2L);
        assertThat(moedaDTO1).isNotEqualTo(moedaDTO2);
        moedaDTO1.setId(null);
        assertThat(moedaDTO1).isNotEqualTo(moedaDTO2);
    }
}
