package com.bankernel.service.mapper;

import static com.bankernel.domain.MoedaAsserts.*;
import static com.bankernel.domain.MoedaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoedaMapperTest {

    private MoedaMapper moedaMapper;

    @BeforeEach
    void setUp() {
        moedaMapper = new MoedaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMoedaSample1();
        var actual = moedaMapper.toEntity(moedaMapper.toDto(expected));
        assertMoedaAllPropertiesEquals(expected, actual);
    }
}
