package com.bankernel.service.mapper;

import static com.bankernel.domain.ContaContabilAsserts.*;
import static com.bankernel.domain.ContaContabilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaContabilMapperTest {

    private ContaContabilMapper contaContabilMapper;

    @BeforeEach
    void setUp() {
        contaContabilMapper = new ContaContabilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContaContabilSample1();
        var actual = contaContabilMapper.toEntity(contaContabilMapper.toDto(expected));
        assertContaContabilAllPropertiesEquals(expected, actual);
    }
}
