package com.bankernel.service.mapper;

import static com.bankernel.domain.SaqueAsserts.*;
import static com.bankernel.domain.SaqueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaqueMapperTest {

    private SaqueMapper saqueMapper;

    @BeforeEach
    void setUp() {
        saqueMapper = new SaqueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSaqueSample1();
        var actual = saqueMapper.toEntity(saqueMapper.toDto(expected));
        assertSaqueAllPropertiesEquals(expected, actual);
    }
}
