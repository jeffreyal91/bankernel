package com.bankernel.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AdministradorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Administrador getAdministradorSample1() {
        return new Administrador().id(1L);
    }

    public static Administrador getAdministradorSample2() {
        return new Administrador().id(2L);
    }

    public static Administrador getAdministradorRandomSampleGenerator() {
        return new Administrador().id(longCount.incrementAndGet());
    }
}
