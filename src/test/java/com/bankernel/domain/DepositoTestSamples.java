package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DepositoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Deposito getDepositoSample1() {
        return new Deposito()
            .id(1L)
            .numeroReferencia("numeroReferencia1")
            .referenciaExterna("referenciaExterna1")
            .descricao("descricao1")
            .motivoRejeicao("motivoRejeicao1")
            .nomeUsuarioFixo("nomeUsuarioFixo1")
            .numeroParcela(1);
    }

    public static Deposito getDepositoSample2() {
        return new Deposito()
            .id(2L)
            .numeroReferencia("numeroReferencia2")
            .referenciaExterna("referenciaExterna2")
            .descricao("descricao2")
            .motivoRejeicao("motivoRejeicao2")
            .nomeUsuarioFixo("nomeUsuarioFixo2")
            .numeroParcela(2);
    }

    public static Deposito getDepositoRandomSampleGenerator() {
        return new Deposito()
            .id(longCount.incrementAndGet())
            .numeroReferencia(UUID.randomUUID().toString())
            .referenciaExterna(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .motivoRejeicao(UUID.randomUUID().toString())
            .nomeUsuarioFixo(UUID.randomUUID().toString())
            .numeroParcela(intCount.incrementAndGet());
    }
}
