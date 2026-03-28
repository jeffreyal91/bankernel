package com.bankernel.service.mapper;

import static com.bankernel.domain.TipoNegocioAsserts.*;
import static com.bankernel.domain.TipoNegocioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoNegocioMapperTest {

    private TipoNegocioMapper tipoNegocioMapper;

    @BeforeEach
    void setUp() {
        tipoNegocioMapper = new TipoNegocioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoNegocioSample1();
        var actual = tipoNegocioMapper.toEntity(tipoNegocioMapper.toDto(expected));
        assertTipoNegocioAllPropertiesEquals(expected, actual);
    }
}
