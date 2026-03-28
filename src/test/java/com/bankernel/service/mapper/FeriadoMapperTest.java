package com.bankernel.service.mapper;

import static com.bankernel.domain.FeriadoAsserts.*;
import static com.bankernel.domain.FeriadoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeriadoMapperTest {

    private FeriadoMapper feriadoMapper;

    @BeforeEach
    void setUp() {
        feriadoMapper = new FeriadoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFeriadoSample1();
        var actual = feriadoMapper.toEntity(feriadoMapper.toDto(expected));
        assertFeriadoAllPropertiesEquals(expected, actual);
    }
}
