package com.bankernel.service.mapper;

import static com.bankernel.domain.SaqueBoletoAsserts.*;
import static com.bankernel.domain.SaqueBoletoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaqueBoletoMapperTest {

    private SaqueBoletoMapper saqueBoletoMapper;

    @BeforeEach
    void setUp() {
        saqueBoletoMapper = new SaqueBoletoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSaqueBoletoSample1();
        var actual = saqueBoletoMapper.toEntity(saqueBoletoMapper.toDto(expected));
        assertSaqueBoletoAllPropertiesEquals(expected, actual);
    }
}
