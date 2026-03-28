package com.bankernel.service.mapper;

import static com.bankernel.domain.HistoricoOperacaoAsserts.*;
import static com.bankernel.domain.HistoricoOperacaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoricoOperacaoMapperTest {

    private HistoricoOperacaoMapper historicoOperacaoMapper;

    @BeforeEach
    void setUp() {
        historicoOperacaoMapper = new HistoricoOperacaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoricoOperacaoSample1();
        var actual = historicoOperacaoMapper.toEntity(historicoOperacaoMapper.toDto(expected));
        assertHistoricoOperacaoAllPropertiesEquals(expected, actual);
    }
}
