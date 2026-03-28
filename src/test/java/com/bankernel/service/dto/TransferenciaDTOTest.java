package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransferenciaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransferenciaDTO.class);
        TransferenciaDTO transferenciaDTO1 = new TransferenciaDTO();
        transferenciaDTO1.setId(1L);
        TransferenciaDTO transferenciaDTO2 = new TransferenciaDTO();
        assertThat(transferenciaDTO1).isNotEqualTo(transferenciaDTO2);
        transferenciaDTO2.setId(transferenciaDTO1.getId());
        assertThat(transferenciaDTO1).isEqualTo(transferenciaDTO2);
        transferenciaDTO2.setId(2L);
        assertThat(transferenciaDTO1).isNotEqualTo(transferenciaDTO2);
        transferenciaDTO1.setId(null);
        assertThat(transferenciaDTO1).isNotEqualTo(transferenciaDTO2);
    }
}
