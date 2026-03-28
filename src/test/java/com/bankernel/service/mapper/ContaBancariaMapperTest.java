package com.bankernel.service.mapper;

import static com.bankernel.domain.ContaBancariaAsserts.*;
import static com.bankernel.domain.ContaBancariaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaBancariaMapperTest {

    private ContaBancariaMapper contaBancariaMapper;

    @BeforeEach
    void setUp() {
        contaBancariaMapper = new ContaBancariaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContaBancariaSample1();
        var actual = contaBancariaMapper.toEntity(contaBancariaMapper.toDto(expected));
        assertContaBancariaAllPropertiesEquals(expected, actual);
    }
}
