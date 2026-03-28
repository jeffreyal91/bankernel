package com.bankernel.service.mapper;

import static com.bankernel.domain.MoedaCarteiraAsserts.*;
import static com.bankernel.domain.MoedaCarteiraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoedaCarteiraMapperTest {

    private MoedaCarteiraMapper moedaCarteiraMapper;

    @BeforeEach
    void setUp() {
        moedaCarteiraMapper = new MoedaCarteiraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMoedaCarteiraSample1();
        var actual = moedaCarteiraMapper.toEntity(moedaCarteiraMapper.toDto(expected));
        assertMoedaCarteiraAllPropertiesEquals(expected, actual);
    }
}
