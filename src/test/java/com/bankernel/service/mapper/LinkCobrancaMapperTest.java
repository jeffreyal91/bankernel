package com.bankernel.service.mapper;

import static com.bankernel.domain.LinkCobrancaAsserts.*;
import static com.bankernel.domain.LinkCobrancaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkCobrancaMapperTest {

    private LinkCobrancaMapper linkCobrancaMapper;

    @BeforeEach
    void setUp() {
        linkCobrancaMapper = new LinkCobrancaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLinkCobrancaSample1();
        var actual = linkCobrancaMapper.toEntity(linkCobrancaMapper.toDto(expected));
        assertLinkCobrancaAllPropertiesEquals(expected, actual);
    }
}
