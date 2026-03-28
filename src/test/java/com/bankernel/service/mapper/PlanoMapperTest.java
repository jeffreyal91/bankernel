package com.bankernel.service.mapper;

import static com.bankernel.domain.PlanoAsserts.*;
import static com.bankernel.domain.PlanoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanoMapperTest {

    private PlanoMapper planoMapper;

    @BeforeEach
    void setUp() {
        planoMapper = new PlanoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlanoSample1();
        var actual = planoMapper.toEntity(planoMapper.toDto(expected));
        assertPlanoAllPropertiesEquals(expected, actual);
    }
}
