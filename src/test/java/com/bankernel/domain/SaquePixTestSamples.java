package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SaquePixTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SaquePix getSaquePixSample1() {
        return new SaquePix()
            .id(1L)
            .identificadorPagamento("identificadorPagamento1")
            .identificadorPontaAPonta("identificadorPontaAPonta1")
            .campoLivre("campoLivre1");
    }

    public static SaquePix getSaquePixSample2() {
        return new SaquePix()
            .id(2L)
            .identificadorPagamento("identificadorPagamento2")
            .identificadorPontaAPonta("identificadorPontaAPonta2")
            .campoLivre("campoLivre2");
    }

    public static SaquePix getSaquePixRandomSampleGenerator() {
        return new SaquePix()
            .id(longCount.incrementAndGet())
            .identificadorPagamento(UUID.randomUUID().toString())
            .identificadorPontaAPonta(UUID.randomUUID().toString())
            .campoLivre(UUID.randomUUID().toString());
    }
}
