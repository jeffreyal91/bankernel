package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EscritorioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Escritorio getEscritorioSample1() {
        return new Escritorio().id(1L).nome("nome1").descricao("descricao1");
    }

    public static Escritorio getEscritorioSample2() {
        return new Escritorio().id(2L).nome("nome2").descricao("descricao2");
    }

    public static Escritorio getEscritorioRandomSampleGenerator() {
        return new Escritorio().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).descricao(UUID.randomUUID().toString());
    }
}
