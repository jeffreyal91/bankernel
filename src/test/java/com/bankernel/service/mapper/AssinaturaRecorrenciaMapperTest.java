package com.bankernel.service.mapper;

import static com.bankernel.domain.AssinaturaRecorrenciaAsserts.*;
import static com.bankernel.domain.AssinaturaRecorrenciaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssinaturaRecorrenciaMapperTest {

    private AssinaturaRecorrenciaMapper assinaturaRecorrenciaMapper;

    @BeforeEach
    void setUp() {
        assinaturaRecorrenciaMapper = new AssinaturaRecorrenciaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssinaturaRecorrenciaSample1();
        var actual = assinaturaRecorrenciaMapper.toEntity(assinaturaRecorrenciaMapper.toDto(expected));
        assertAssinaturaRecorrenciaAllPropertiesEquals(expected, actual);
    }
}
