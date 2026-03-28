package com.bankernel.service.mapper;

import static com.bankernel.domain.SaquePixAsserts.*;
import static com.bankernel.domain.SaquePixTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaquePixMapperTest {

    private SaquePixMapper saquePixMapper;

    @BeforeEach
    void setUp() {
        saquePixMapper = new SaquePixMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSaquePixSample1();
        var actual = saquePixMapper.toEntity(saquePixMapper.toDto(expected));
        assertSaquePixAllPropertiesEquals(expected, actual);
    }
}
