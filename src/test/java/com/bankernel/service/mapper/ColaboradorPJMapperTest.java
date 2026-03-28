package com.bankernel.service.mapper;

import static com.bankernel.domain.ColaboradorPJAsserts.*;
import static com.bankernel.domain.ColaboradorPJTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColaboradorPJMapperTest {

    private ColaboradorPJMapper colaboradorPJMapper;

    @BeforeEach
    void setUp() {
        colaboradorPJMapper = new ColaboradorPJMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getColaboradorPJSample1();
        var actual = colaboradorPJMapper.toEntity(colaboradorPJMapper.toDto(expected));
        assertColaboradorPJAllPropertiesEquals(expected, actual);
    }
}
