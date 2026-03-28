package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PlanoRecorrenciaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PlanoRecorrencia getPlanoRecorrenciaSample1() {
        return new PlanoRecorrencia()
            .id(1L)
            .identificadorExterno("identificadorExterno1")
            .numeroReferencia("numeroReferencia1")
            .nome("nome1")
            .descricao("descricao1")
            .diasTeste(1)
            .intervalo(1)
            .parcelas(1)
            .tentativas(1);
    }

    public static PlanoRecorrencia getPlanoRecorrenciaSample2() {
        return new PlanoRecorrencia()
            .id(2L)
            .identificadorExterno("identificadorExterno2")
            .numeroReferencia("numeroReferencia2")
            .nome("nome2")
            .descricao("descricao2")
            .diasTeste(2)
            .intervalo(2)
            .parcelas(2)
            .tentativas(2);
    }

    public static PlanoRecorrencia getPlanoRecorrenciaRandomSampleGenerator() {
        return new PlanoRecorrencia()
            .id(longCount.incrementAndGet())
            .identificadorExterno(UUID.randomUUID().toString())
            .numeroReferencia(UUID.randomUUID().toString())
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .diasTeste(intCount.incrementAndGet())
            .intervalo(intCount.incrementAndGet())
            .parcelas(intCount.incrementAndGet())
            .tentativas(intCount.incrementAndGet());
    }
}
