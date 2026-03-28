package com.bankernel.service.mapper;

import static com.bankernel.domain.LinkPagamentoAsserts.*;
import static com.bankernel.domain.LinkPagamentoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkPagamentoMapperTest {

    private LinkPagamentoMapper linkPagamentoMapper;

    @BeforeEach
    void setUp() {
        linkPagamentoMapper = new LinkPagamentoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLinkPagamentoSample1();
        var actual = linkPagamentoMapper.toEntity(linkPagamentoMapper.toDto(expected));
        assertLinkPagamentoAllPropertiesEquals(expected, actual);
    }
}
