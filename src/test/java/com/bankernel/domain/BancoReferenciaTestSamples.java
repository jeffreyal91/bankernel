package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BancoReferenciaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BancoReferencia getBancoReferenciaSample1() {
        return new BancoReferencia().id(1L).codigo("codigo1").nome("nome1").ispb("ispb1");
    }

    public static BancoReferencia getBancoReferenciaSample2() {
        return new BancoReferencia().id(2L).codigo("codigo2").nome("nome2").ispb("ispb2");
    }

    public static BancoReferencia getBancoReferenciaRandomSampleGenerator() {
        return new BancoReferencia()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nome(UUID.randomUUID().toString())
            .ispb(UUID.randomUUID().toString());
    }
}
