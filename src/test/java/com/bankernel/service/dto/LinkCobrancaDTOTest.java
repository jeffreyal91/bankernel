package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinkCobrancaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkCobrancaDTO.class);
        LinkCobrancaDTO linkCobrancaDTO1 = new LinkCobrancaDTO();
        linkCobrancaDTO1.setId(1L);
        LinkCobrancaDTO linkCobrancaDTO2 = new LinkCobrancaDTO();
        assertThat(linkCobrancaDTO1).isNotEqualTo(linkCobrancaDTO2);
        linkCobrancaDTO2.setId(linkCobrancaDTO1.getId());
        assertThat(linkCobrancaDTO1).isEqualTo(linkCobrancaDTO2);
        linkCobrancaDTO2.setId(2L);
        assertThat(linkCobrancaDTO1).isNotEqualTo(linkCobrancaDTO2);
        linkCobrancaDTO1.setId(null);
        assertThat(linkCobrancaDTO1).isNotEqualTo(linkCobrancaDTO2);
    }
}
