package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeriadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Feriado getFeriadoSample1() {
        return new Feriado().id(1L).descricao("descricao1");
    }

    public static Feriado getFeriadoSample2() {
        return new Feriado().id(2L).descricao("descricao2");
    }

    public static Feriado getFeriadoRandomSampleGenerator() {
        return new Feriado().id(longCount.incrementAndGet()).descricao(UUID.randomUUID().toString());
    }
}
