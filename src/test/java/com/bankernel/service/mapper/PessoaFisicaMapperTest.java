package com.bankernel.service.mapper;

import static com.bankernel.domain.PessoaFisicaAsserts.*;
import static com.bankernel.domain.PessoaFisicaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PessoaFisicaMapperTest {

    private PessoaFisicaMapper pessoaFisicaMapper;

    @BeforeEach
    void setUp() {
        pessoaFisicaMapper = new PessoaFisicaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPessoaFisicaSample1();
        var actual = pessoaFisicaMapper.toEntity(pessoaFisicaMapper.toDto(expected));
        assertPessoaFisicaAllPropertiesEquals(expected, actual);
    }
}
