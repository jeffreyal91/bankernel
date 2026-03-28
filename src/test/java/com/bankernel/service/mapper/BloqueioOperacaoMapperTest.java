package com.bankernel.service.mapper;

import static com.bankernel.domain.BloqueioOperacaoAsserts.*;
import static com.bankernel.domain.BloqueioOperacaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BloqueioOperacaoMapperTest {

    private BloqueioOperacaoMapper bloqueioOperacaoMapper;

    @BeforeEach
    void setUp() {
        bloqueioOperacaoMapper = new BloqueioOperacaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBloqueioOperacaoSample1();
        var actual = bloqueioOperacaoMapper.toEntity(bloqueioOperacaoMapper.toDto(expected));
        assertBloqueioOperacaoAllPropertiesEquals(expected, actual);
    }
}
