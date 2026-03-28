package com.bankernel.service.mapper;

import static com.bankernel.domain.PermissaoColaboradorAsserts.*;
import static com.bankernel.domain.PermissaoColaboradorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissaoColaboradorMapperTest {

    private PermissaoColaboradorMapper permissaoColaboradorMapper;

    @BeforeEach
    void setUp() {
        permissaoColaboradorMapper = new PermissaoColaboradorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPermissaoColaboradorSample1();
        var actual = permissaoColaboradorMapper.toEntity(permissaoColaboradorMapper.toDto(expected));
        assertPermissaoColaboradorAllPropertiesEquals(expected, actual);
    }
}
