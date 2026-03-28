package com.bankernel.service.mapper;

import static com.bankernel.domain.IntegracaoLogAsserts.*;
import static com.bankernel.domain.IntegracaoLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegracaoLogMapperTest {

    private IntegracaoLogMapper integracaoLogMapper;

    @BeforeEach
    void setUp() {
        integracaoLogMapper = new IntegracaoLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIntegracaoLogSample1();
        var actual = integracaoLogMapper.toEntity(integracaoLogMapper.toDto(expected));
        assertIntegracaoLogAllPropertiesEquals(expected, actual);
    }
}
