package com.bankernel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinkPagamentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkPagamentoDTO.class);
        LinkPagamentoDTO linkPagamentoDTO1 = new LinkPagamentoDTO();
        linkPagamentoDTO1.setId(1L);
        LinkPagamentoDTO linkPagamentoDTO2 = new LinkPagamentoDTO();
        assertThat(linkPagamentoDTO1).isNotEqualTo(linkPagamentoDTO2);
        linkPagamentoDTO2.setId(linkPagamentoDTO1.getId());
        assertThat(linkPagamentoDTO1).isEqualTo(linkPagamentoDTO2);
        linkPagamentoDTO2.setId(2L);
        assertThat(linkPagamentoDTO1).isNotEqualTo(linkPagamentoDTO2);
        linkPagamentoDTO1.setId(null);
        assertThat(linkPagamentoDTO1).isNotEqualTo(linkPagamentoDTO2);
    }
}
