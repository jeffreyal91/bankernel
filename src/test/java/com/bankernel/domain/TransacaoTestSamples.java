package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transacao getTransacaoSample1() {
        return new Transacao().id(1L).descricao("descricao1").tipoEntidadeOrigem("tipoEntidadeOrigem1").idEntidadeOrigem(1L);
    }

    public static Transacao getTransacaoSample2() {
        return new Transacao().id(2L).descricao("descricao2").tipoEntidadeOrigem("tipoEntidadeOrigem2").idEntidadeOrigem(2L);
    }

    public static Transacao getTransacaoRandomSampleGenerator() {
        return new Transacao()
            .id(longCount.incrementAndGet())
            .descricao(UUID.randomUUID().toString())
            .tipoEntidadeOrigem(UUID.randomUUID().toString())
            .idEntidadeOrigem(longCount.incrementAndGet());
    }
}
