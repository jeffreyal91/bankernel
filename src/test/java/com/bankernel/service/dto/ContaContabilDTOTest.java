package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaContabilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaContabilDTO.class);
        ContaContabilDTO contaContabilDTO1 = new ContaContabilDTO();
        contaContabilDTO1.setId(1L);
        ContaContabilDTO contaContabilDTO2 = new ContaContabilDTO();
        assertThat(contaContabilDTO1).isNotEqualTo(contaContabilDTO2);
        contaContabilDTO2.setId(contaContabilDTO1.getId());
        assertThat(contaContabilDTO1).isEqualTo(contaContabilDTO2);
        contaContabilDTO2.setId(2L);
        assertThat(contaContabilDTO1).isNotEqualTo(contaContabilDTO2);
        contaContabilDTO1.setId(null);
        assertThat(contaContabilDTO1).isNotEqualTo(contaContabilDTO2);
    }
}
