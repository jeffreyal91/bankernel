package com.bankernel.service.mapper;

import static com.bankernel.domain.ProfissaoAsserts.*;
import static com.bankernel.domain.ProfissaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfissaoMapperTest {

    private ProfissaoMapper profissaoMapper;

    @BeforeEach
    void setUp() {
        profissaoMapper = new ProfissaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfissaoSample1();
        var actual = profissaoMapper.toEntity(profissaoMapper.toDto(expected));
        assertProfissaoAllPropertiesEquals(expected, actual);
    }
}
