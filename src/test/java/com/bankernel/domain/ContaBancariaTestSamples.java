package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContaBancariaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContaBancaria getContaBancariaSample1() {
        return new ContaBancaria()
            .id(1L)
            .nomeTitular("nomeTitular1")
            .numeroConta("numeroConta1")
            .agencia("agencia1")
            .nomeBanco("nomeBanco1")
            .codigoBanco("codigoBanco1")
            .ispb("ispb1")
            .codigoSwift("codigoSwift1");
    }

    public static ContaBancaria getContaBancariaSample2() {
        return new ContaBancaria()
            .id(2L)
            .nomeTitular("nomeTitular2")
            .numeroConta("numeroConta2")
            .agencia("agencia2")
            .nomeBanco("nomeBanco2")
            .codigoBanco("codigoBanco2")
            .ispb("ispb2")
            .codigoSwift("codigoSwift2");
    }

    public static ContaBancaria getContaBancariaRandomSampleGenerator() {
        return new ContaBancaria()
            .id(longCount.incrementAndGet())
            .nomeTitular(UUID.randomUUID().toString())
            .numeroConta(UUID.randomUUID().toString())
            .agencia(UUID.randomUUID().toString())
            .nomeBanco(UUID.randomUUID().toString())
            .codigoBanco(UUID.randomUUID().toString())
            .ispb(UUID.randomUUID().toString())
            .codigoSwift(UUID.randomUUID().toString());
    }
}
