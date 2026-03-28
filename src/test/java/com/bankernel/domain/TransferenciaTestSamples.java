package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransferenciaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transferencia getTransferenciaSample1() {
        return new Transferencia()
            .id(1L)
            .chaveInterna("chaveInterna1")
            .descricao("descricao1")
            .motivoRejeicao("motivoRejeicao1")
            .numeroReferencia("numeroReferencia1");
    }

    public static Transferencia getTransferenciaSample2() {
        return new Transferencia()
            .id(2L)
            .chaveInterna("chaveInterna2")
            .descricao("descricao2")
            .motivoRejeicao("motivoRejeicao2")
            .numeroReferencia("numeroReferencia2");
    }

    public static Transferencia getTransferenciaRandomSampleGenerator() {
        return new Transferencia()
            .id(longCount.incrementAndGet())
            .chaveInterna(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .motivoRejeicao(UUID.randomUUID().toString())
            .numeroReferencia(UUID.randomUUID().toString());
    }
}
