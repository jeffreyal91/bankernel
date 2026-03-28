package com.bankernel.service.mapper;

import static com.bankernel.domain.TipoOperacaoAsserts.*;
import static com.bankernel.domain.TipoOperacaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoOperacaoMapperTest {

    private TipoOperacaoMapper tipoOperacaoMapper;

    @BeforeEach
    void setUp() {
        tipoOperacaoMapper = new TipoOperacaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoOperacaoSample1();
        var actual = tipoOperacaoMapper.toEntity(tipoOperacaoMapper.toDto(expected));
        assertTipoOperacaoAllPropertiesEquals(expected, actual);
    }
}
