package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarteiraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarteiraDTO.class);
        CarteiraDTO carteiraDTO1 = new CarteiraDTO();
        carteiraDTO1.setId(1L);
        CarteiraDTO carteiraDTO2 = new CarteiraDTO();
        assertThat(carteiraDTO1).isNotEqualTo(carteiraDTO2);
        carteiraDTO2.setId(carteiraDTO1.getId());
        assertThat(carteiraDTO1).isEqualTo(carteiraDTO2);
        carteiraDTO2.setId(2L);
        assertThat(carteiraDTO1).isNotEqualTo(carteiraDTO2);
        carteiraDTO1.setId(null);
        assertThat(carteiraDTO1).isNotEqualTo(carteiraDTO2);
    }
}
