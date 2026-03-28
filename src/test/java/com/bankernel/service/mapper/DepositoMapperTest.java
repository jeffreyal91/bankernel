package com.bankernel.service.mapper;

import static com.bankernel.domain.DepositoAsserts.*;
import static com.bankernel.domain.DepositoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepositoMapperTest {

    private DepositoMapper depositoMapper;

    @BeforeEach
    void setUp() {
        depositoMapper = new DepositoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDepositoSample1();
        var actual = depositoMapper.toEntity(depositoMapper.toDto(expected));
        assertDepositoAllPropertiesEquals(expected, actual);
    }
}
