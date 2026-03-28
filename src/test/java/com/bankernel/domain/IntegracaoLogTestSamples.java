package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IntegracaoLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IntegracaoLog getIntegracaoLogSample1() {
        return new IntegracaoLog()
            .id(1L)
            .provider("provider1")
            .operacao("operacao1")
            .statusHttp(1)
            .mensagemErro("mensagemErro1")
            .duracaoMs(1L)
            .tipoEntidadeOrigem("tipoEntidadeOrigem1")
            .idEntidadeOrigem(1L);
    }

    public static IntegracaoLog getIntegracaoLogSample2() {
        return new IntegracaoLog()
            .id(2L)
            .provider("provider2")
            .operacao("operacao2")
            .statusHttp(2)
            .mensagemErro("mensagemErro2")
            .duracaoMs(2L)
            .tipoEntidadeOrigem("tipoEntidadeOrigem2")
            .idEntidadeOrigem(2L);
    }

    public static IntegracaoLog getIntegracaoLogRandomSampleGenerator() {
        return new IntegracaoLog()
            .id(longCount.incrementAndGet())
            .provider(UUID.randomUUID().toString())
            .operacao(UUID.randomUUID().toString())
            .statusHttp(intCount.incrementAndGet())
            .mensagemErro(UUID.randomUUID().toString())
            .duracaoMs(longCount.incrementAndGet())
            .tipoEntidadeOrigem(UUID.randomUUID().toString())
            .idEntidadeOrigem(longCount.incrementAndGet());
    }
}
