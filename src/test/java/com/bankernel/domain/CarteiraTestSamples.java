package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarteiraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Carteira getCarteiraSample1() {
        return new Carteira().id(1L).numeroConta("numeroConta1");
    }

    public static Carteira getCarteiraSample2() {
        return new Carteira().id(2L).numeroConta("numeroConta2");
    }

    public static Carteira getCarteiraRandomSampleGenerator() {
        return new Carteira().id(longCount.incrementAndGet()).numeroConta(UUID.randomUUID().toString());
    }
}
