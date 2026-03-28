package com.bankernel.service.mapper;

import static com.bankernel.domain.TransacaoAsserts.*;
import static com.bankernel.domain.TransacaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransacaoMapperTest {

    private TransacaoMapper transacaoMapper;

    @BeforeEach
    void setUp() {
        transacaoMapper = new TransacaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransacaoSample1();
        var actual = transacaoMapper.toEntity(transacaoMapper.toDto(expected));
        assertTransacaoAllPropertiesEquals(expected, actual);
    }
}
