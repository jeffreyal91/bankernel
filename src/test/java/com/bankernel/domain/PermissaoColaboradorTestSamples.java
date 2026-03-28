package com.bankernel.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PermissaoColaboradorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PermissaoColaborador getPermissaoColaboradorSample1() {
        return new PermissaoColaborador().id(1L);
    }

    public static PermissaoColaborador getPermissaoColaboradorSample2() {
        return new PermissaoColaborador().id(2L);
    }

    public static PermissaoColaborador getPermissaoColaboradorRandomSampleGenerator() {
        return new PermissaoColaborador().id(longCount.incrementAndGet());
    }
}
