package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoNegocioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoNegocio getTipoNegocioSample1() {
        return new TipoNegocio().id(1L).nome("nome1");
    }

    public static TipoNegocio getTipoNegocioSample2() {
        return new TipoNegocio().id(2L).nome("nome2");
    }

    public static TipoNegocio getTipoNegocioRandomSampleGenerator() {
        return new TipoNegocio().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
