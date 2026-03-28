package com.bankernel.service.mapper;

import static com.bankernel.domain.CobrancaAsserts.*;
import static com.bankernel.domain.CobrancaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CobrancaMapperTest {

    private CobrancaMapper cobrancaMapper;

    @BeforeEach
    void setUp() {
        cobrancaMapper = new CobrancaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCobrancaSample1();
        var actual = cobrancaMapper.toEntity(cobrancaMapper.toDto(expected));
        assertCobrancaAllPropertiesEquals(expected, actual);
    }
}
