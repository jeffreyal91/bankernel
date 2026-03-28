package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CobrancaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cobranca getCobrancaSample1() {
        return new Cobranca()
            .id(1L)
            .idProdutoExterno("idProdutoExterno1")
            .nomeProdutoExterno("nomeProdutoExterno1")
            .nomeUsuarioFixo("nomeUsuarioFixo1")
            .chaveCobranca("chaveCobranca1")
            .identificadorExterno("identificadorExterno1");
    }

    public static Cobranca getCobrancaSample2() {
        return new Cobranca()
            .id(2L)
            .idProdutoExterno("idProdutoExterno2")
            .nomeProdutoExterno("nomeProdutoExterno2")
            .nomeUsuarioFixo("nomeUsuarioFixo2")
            .chaveCobranca("chaveCobranca2")
            .identificadorExterno("identificadorExterno2");
    }

    public static Cobranca getCobrancaRandomSampleGenerator() {
        return new Cobranca()
            .id(longCount.incrementAndGet())
            .idProdutoExterno(UUID.randomUUID().toString())
            .nomeProdutoExterno(UUID.randomUUID().toString())
            .nomeUsuarioFixo(UUID.randomUUID().toString())
            .chaveCobranca(UUID.randomUUID().toString())
            .identificadorExterno(UUID.randomUUID().toString());
    }
}
