package com.bankernel.service.mapper;

import static com.bankernel.domain.PlanoRecorrenciaAsserts.*;
import static com.bankernel.domain.PlanoRecorrenciaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanoRecorrenciaMapperTest {

    private PlanoRecorrenciaMapper planoRecorrenciaMapper;

    @BeforeEach
    void setUp() {
        planoRecorrenciaMapper = new PlanoRecorrenciaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlanoRecorrenciaSample1();
        var actual = planoRecorrenciaMapper.toEntity(planoRecorrenciaMapper.toDto(expected));
        assertPlanoRecorrenciaAllPropertiesEquals(expected, actual);
    }
}
