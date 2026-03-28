package com.bankernel.service.mapper;

import static com.bankernel.domain.RegistroIntegracaoAsserts.*;
import static com.bankernel.domain.RegistroIntegracaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistroIntegracaoMapperTest {

    private RegistroIntegracaoMapper registroIntegracaoMapper;

    @BeforeEach
    void setUp() {
        registroIntegracaoMapper = new RegistroIntegracaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRegistroIntegracaoSample1();
        var actual = registroIntegracaoMapper.toEntity(registroIntegracaoMapper.toDto(expected));
        assertRegistroIntegracaoAllPropertiesEquals(expected, actual);
    }
}
