package com.bankernel.service.mapper;

import static com.bankernel.domain.BancoReferenciaAsserts.*;
import static com.bankernel.domain.BancoReferenciaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BancoReferenciaMapperTest {

    private BancoReferenciaMapper bancoReferenciaMapper;

    @BeforeEach
    void setUp() {
        bancoReferenciaMapper = new BancoReferenciaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBancoReferenciaSample1();
        var actual = bancoReferenciaMapper.toEntity(bancoReferenciaMapper.toDto(expected));
        assertBancoReferenciaAllPropertiesEquals(expected, actual);
    }
}
