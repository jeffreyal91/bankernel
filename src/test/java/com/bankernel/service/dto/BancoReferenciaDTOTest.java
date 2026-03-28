package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BancoReferenciaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BancoReferenciaDTO.class);
        BancoReferenciaDTO bancoReferenciaDTO1 = new BancoReferenciaDTO();
        bancoReferenciaDTO1.setId(1L);
        BancoReferenciaDTO bancoReferenciaDTO2 = new BancoReferenciaDTO();
        assertThat(bancoReferenciaDTO1).isNotEqualTo(bancoReferenciaDTO2);
        bancoReferenciaDTO2.setId(bancoReferenciaDTO1.getId());
        assertThat(bancoReferenciaDTO1).isEqualTo(bancoReferenciaDTO2);
        bancoReferenciaDTO2.setId(2L);
        assertThat(bancoReferenciaDTO1).isNotEqualTo(bancoReferenciaDTO2);
        bancoReferenciaDTO1.setId(null);
        assertThat(bancoReferenciaDTO1).isNotEqualTo(bancoReferenciaDTO2);
    }
}
