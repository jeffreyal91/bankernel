package com.bankernel.service.mapper;

import static com.bankernel.domain.ConfiguracaoSistemaAsserts.*;
import static com.bankernel.domain.ConfiguracaoSistemaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfiguracaoSistemaMapperTest {

    private ConfiguracaoSistemaMapper configuracaoSistemaMapper;

    @BeforeEach
    void setUp() {
        configuracaoSistemaMapper = new ConfiguracaoSistemaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConfiguracaoSistemaSample1();
        var actual = configuracaoSistemaMapper.toEntity(configuracaoSistemaMapper.toDto(expected));
        assertConfiguracaoSistemaAllPropertiesEquals(expected, actual);
    }
}
