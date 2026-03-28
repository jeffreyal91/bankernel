package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SaqueBoletoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SaqueBoleto getSaqueBoletoSample1() {
        return new SaqueBoleto().id(1L).codigoBarras("codigoBarras1").campoLivre("campoLivre1");
    }

    public static SaqueBoleto getSaqueBoletoSample2() {
        return new SaqueBoleto().id(2L).codigoBarras("codigoBarras2").campoLivre("campoLivre2");
    }

    public static SaqueBoleto getSaqueBoletoRandomSampleGenerator() {
        return new SaqueBoleto()
            .id(longCount.incrementAndGet())
            .codigoBarras(UUID.randomUUID().toString())
            .campoLivre(UUID.randomUUID().toString());
    }
}
