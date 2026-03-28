package com.bankernel.service.mapper;

import static com.bankernel.domain.DepositoPixAsserts.*;
import static com.bankernel.domain.DepositoPixTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepositoPixMapperTest {

    private DepositoPixMapper depositoPixMapper;

    @BeforeEach
    void setUp() {
        depositoPixMapper = new DepositoPixMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDepositoPixSample1();
        var actual = depositoPixMapper.toEntity(depositoPixMapper.toDto(expected));
        assertDepositoPixAllPropertiesEquals(expected, actual);
    }
}
