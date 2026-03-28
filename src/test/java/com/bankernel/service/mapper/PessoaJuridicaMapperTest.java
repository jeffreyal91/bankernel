package com.bankernel.service.mapper;

import static com.bankernel.domain.PessoaJuridicaAsserts.*;
import static com.bankernel.domain.PessoaJuridicaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PessoaJuridicaMapperTest {

    private PessoaJuridicaMapper pessoaJuridicaMapper;

    @BeforeEach
    void setUp() {
        pessoaJuridicaMapper = new PessoaJuridicaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPessoaJuridicaSample1();
        var actual = pessoaJuridicaMapper.toEntity(pessoaJuridicaMapper.toDto(expected));
        assertPessoaJuridicaAllPropertiesEquals(expected, actual);
    }
}
