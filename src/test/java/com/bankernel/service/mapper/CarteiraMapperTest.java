package com.bankernel.service.mapper;

import static com.bankernel.domain.CarteiraAsserts.*;
import static com.bankernel.domain.CarteiraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarteiraMapperTest {

    private CarteiraMapper carteiraMapper;

    @BeforeEach
    void setUp() {
        carteiraMapper = new CarteiraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCarteiraSample1();
        var actual = carteiraMapper.toEntity(carteiraMapper.toDto(expected));
        assertCarteiraAllPropertiesEquals(expected, actual);
    }
}
