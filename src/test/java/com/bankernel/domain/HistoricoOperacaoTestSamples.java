package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoricoOperacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoricoOperacao getHistoricoOperacaoSample1() {
        return new HistoricoOperacao()
            .id(1L)
            .descricao("descricao1")
            .numeroReferencia("numeroReferencia1")
            .tipoEntidadeOrigem("tipoEntidadeOrigem1")
            .idEntidadeOrigem(1L);
    }

    public static HistoricoOperacao getHistoricoOperacaoSample2() {
        return new HistoricoOperacao()
            .id(2L)
            .descricao("descricao2")
            .numeroReferencia("numeroReferencia2")
            .tipoEntidadeOrigem("tipoEntidadeOrigem2")
            .idEntidadeOrigem(2L);
    }

    public static HistoricoOperacao getHistoricoOperacaoRandomSampleGenerator() {
        return new HistoricoOperacao()
            .id(longCount.incrementAndGet())
            .descricao(UUID.randomUUID().toString())
            .numeroReferencia(UUID.randomUUID().toString())
            .tipoEntidadeOrigem(UUID.randomUUID().toString())
            .idEntidadeOrigem(longCount.incrementAndGet());
    }
}
