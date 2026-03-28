package com.bankernel.service.mapper;

import static com.bankernel.domain.AdministradorAsserts.*;
import static com.bankernel.domain.AdministradorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdministradorMapperTest {

    private AdministradorMapper administradorMapper;

    @BeforeEach
    void setUp() {
        administradorMapper = new AdministradorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdministradorSample1();
        var actual = administradorMapper.toEntity(administradorMapper.toDto(expected));
        assertAdministradorAllPropertiesEquals(expected, actual);
    }
}
