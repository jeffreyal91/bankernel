package com.bankernel.service.mapper;

import static com.bankernel.domain.EscritorioAsserts.*;
import static com.bankernel.domain.EscritorioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EscritorioMapperTest {

    private EscritorioMapper escritorioMapper;

    @BeforeEach
    void setUp() {
        escritorioMapper = new EscritorioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEscritorioSample1();
        var actual = escritorioMapper.toEntity(escritorioMapper.toDto(expected));
        assertEscritorioAllPropertiesEquals(expected, actual);
    }
}
