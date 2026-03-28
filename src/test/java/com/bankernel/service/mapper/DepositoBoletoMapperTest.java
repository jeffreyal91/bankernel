package com.bankernel.service.mapper;

import static com.bankernel.domain.DepositoBoletoAsserts.*;
import static com.bankernel.domain.DepositoBoletoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepositoBoletoMapperTest {

    private DepositoBoletoMapper depositoBoletoMapper;

    @BeforeEach
    void setUp() {
        depositoBoletoMapper = new DepositoBoletoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDepositoBoletoSample1();
        var actual = depositoBoletoMapper.toEntity(depositoBoletoMapper.toDto(expected));
        assertDepositoBoletoAllPropertiesEquals(expected, actual);
    }
}
