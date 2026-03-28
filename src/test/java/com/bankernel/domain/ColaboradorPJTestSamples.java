package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ColaboradorPJTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ColaboradorPJ getColaboradorPJSample1() {
        return new ColaboradorPJ().id(1L).departamento("departamento1");
    }

    public static ColaboradorPJ getColaboradorPJSample2() {
        return new ColaboradorPJ().id(2L).departamento("departamento2");
    }

    public static ColaboradorPJ getColaboradorPJRandomSampleGenerator() {
        return new ColaboradorPJ().id(longCount.incrementAndGet()).departamento(UUID.randomUUID().toString());
    }
}
