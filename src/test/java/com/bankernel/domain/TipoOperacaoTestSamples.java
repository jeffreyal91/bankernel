package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoOperacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoOperacao getTipoOperacaoSample1() {
        return new TipoOperacao().id(1L).nome("nome1").descricao("descricao1");
    }

    public static TipoOperacao getTipoOperacaoSample2() {
        return new TipoOperacao().id(2L).nome("nome2").descricao("descricao2");
    }

    public static TipoOperacao getTipoOperacaoRandomSampleGenerator() {
        return new TipoOperacao()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString());
    }
}
