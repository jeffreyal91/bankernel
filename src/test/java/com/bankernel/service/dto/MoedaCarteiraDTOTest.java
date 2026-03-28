package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoedaCarteiraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoedaCarteiraDTO.class);
        MoedaCarteiraDTO moedaCarteiraDTO1 = new MoedaCarteiraDTO();
        moedaCarteiraDTO1.setId(1L);
        MoedaCarteiraDTO moedaCarteiraDTO2 = new MoedaCarteiraDTO();
        assertThat(moedaCarteiraDTO1).isNotEqualTo(moedaCarteiraDTO2);
        moedaCarteiraDTO2.setId(moedaCarteiraDTO1.getId());
        assertThat(moedaCarteiraDTO1).isEqualTo(moedaCarteiraDTO2);
        moedaCarteiraDTO2.setId(2L);
        assertThat(moedaCarteiraDTO1).isNotEqualTo(moedaCarteiraDTO2);
        moedaCarteiraDTO1.setId(null);
        assertThat(moedaCarteiraDTO1).isNotEqualTo(moedaCarteiraDTO2);
    }
}
