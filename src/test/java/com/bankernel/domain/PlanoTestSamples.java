package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlanoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Plano getPlanoSample1() {
        return new Plano().id(1L).nome("nome1").descricao("descricao1");
    }

    public static Plano getPlanoSample2() {
        return new Plano().id(2L).nome("nome2").descricao("descricao2");
    }

    public static Plano getPlanoRandomSampleGenerator() {
        return new Plano().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).descricao(UUID.randomUUID().toString());
    }
}
