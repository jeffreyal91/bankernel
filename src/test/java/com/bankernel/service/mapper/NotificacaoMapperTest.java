package com.bankernel.service.mapper;

import static com.bankernel.domain.NotificacaoAsserts.*;
import static com.bankernel.domain.NotificacaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificacaoMapperTest {

    private NotificacaoMapper notificacaoMapper;

    @BeforeEach
    void setUp() {
        notificacaoMapper = new NotificacaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificacaoSample1();
        var actual = notificacaoMapper.toEntity(notificacaoMapper.toDto(expected));
        assertNotificacaoAllPropertiesEquals(expected, actual);
    }
}
