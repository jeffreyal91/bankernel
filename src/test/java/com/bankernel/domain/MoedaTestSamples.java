package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MoedaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Moeda getMoedaSample1() {
        return new Moeda().id(1L).nome("nome1").descricao("descricao1");
    }

    public static Moeda getMoedaSample2() {
        return new Moeda().id(2L).nome("nome2").descricao("descricao2");
    }

    public static Moeda getMoedaRandomSampleGenerator() {
        return new Moeda().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).descricao(UUID.randomUUID().toString());
    }
}
