package com.bankernel.service.mapper;

import static com.bankernel.domain.LancamentoContabilAsserts.*;
import static com.bankernel.domain.LancamentoContabilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LancamentoContabilMapperTest {

    private LancamentoContabilMapper lancamentoContabilMapper;

    @BeforeEach
    void setUp() {
        lancamentoContabilMapper = new LancamentoContabilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLancamentoContabilSample1();
        var actual = lancamentoContabilMapper.toEntity(lancamentoContabilMapper.toDto(expected));
        assertLancamentoContabilAllPropertiesEquals(expected, actual);
    }
}
