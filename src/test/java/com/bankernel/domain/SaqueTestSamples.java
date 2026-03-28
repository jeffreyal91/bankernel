package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SaqueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Saque getSaqueSample1() {
        return new Saque()
            .id(1L)
            .descricao("descricao1")
            .numeroReferencia("numeroReferencia1")
            .motivoRejeicao("motivoRejeicao1")
            .nomeUsuarioFixo("nomeUsuarioFixo1");
    }

    public static Saque getSaqueSample2() {
        return new Saque()
            .id(2L)
            .descricao("descricao2")
            .numeroReferencia("numeroReferencia2")
            .motivoRejeicao("motivoRejeicao2")
            .nomeUsuarioFixo("nomeUsuarioFixo2");
    }

    public static Saque getSaqueRandomSampleGenerator() {
        return new Saque()
            .id(longCount.incrementAndGet())
            .descricao(UUID.randomUUID().toString())
            .numeroReferencia(UUID.randomUUID().toString())
            .motivoRejeicao(UUID.randomUUID().toString())
            .nomeUsuarioFixo(UUID.randomUUID().toString());
    }
}
