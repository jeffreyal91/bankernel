package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContaContabilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContaContabil getContaContabilSample1() {
        return new ContaContabil().id(1L).codigo("codigo1").nome("nome1").descricao("descricao1");
    }

    public static ContaContabil getContaContabilSample2() {
        return new ContaContabil().id(2L).codigo("codigo2").nome("nome2").descricao("descricao2");
    }

    public static ContaContabil getContaContabilRandomSampleGenerator() {
        return new ContaContabil()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString());
    }
}
