package com.bankernel.service.mapper;

import static com.bankernel.domain.TransferenciaAsserts.*;
import static com.bankernel.domain.TransferenciaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransferenciaMapperTest {

    private TransferenciaMapper transferenciaMapper;

    @BeforeEach
    void setUp() {
        transferenciaMapper = new TransferenciaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransferenciaSample1();
        var actual = transferenciaMapper.toEntity(transferenciaMapper.toDto(expected));
        assertTransferenciaAllPropertiesEquals(expected, actual);
    }
}
