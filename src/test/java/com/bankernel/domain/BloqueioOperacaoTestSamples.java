package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BloqueioOperacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BloqueioOperacao getBloqueioOperacaoSample1() {
        return new BloqueioOperacao().id(1L).horaInicio("horaInicio1").horaFim("horaFim1").motivo("motivo1");
    }

    public static BloqueioOperacao getBloqueioOperacaoSample2() {
        return new BloqueioOperacao().id(2L).horaInicio("horaInicio2").horaFim("horaFim2").motivo("motivo2");
    }

    public static BloqueioOperacao getBloqueioOperacaoRandomSampleGenerator() {
        return new BloqueioOperacao()
            .id(longCount.incrementAndGet())
            .horaInicio(UUID.randomUUID().toString())
            .horaFim(UUID.randomUUID().toString())
            .motivo(UUID.randomUUID().toString());
    }
}
