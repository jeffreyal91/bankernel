package com.bankernel.service.mapper;

import static com.bankernel.domain.EnderecoAsserts.*;
import static com.bankernel.domain.EnderecoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnderecoMapperTest {

    private EnderecoMapper enderecoMapper;

    @BeforeEach
    void setUp() {
        enderecoMapper = new EnderecoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnderecoSample1();
        var actual = enderecoMapper.toEntity(enderecoMapper.toDto(expected));
        assertEnderecoAllPropertiesEquals(expected, actual);
    }
}
