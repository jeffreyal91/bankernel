package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepositoBoletoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DepositoBoleto getDepositoBoletoSample1() {
        return new DepositoBoleto()
            .id(1L)
            .codigoBarras("codigoBarras1")
            .linhaDigitavel("linhaDigitavel1")
            .pagadorNome("pagadorNome1")
            .pagadorCpf("pagadorCpf1")
            .pagadorCnpj("pagadorCnpj1");
    }

    public static DepositoBoleto getDepositoBoletoSample2() {
        return new DepositoBoleto()
            .id(2L)
            .codigoBarras("codigoBarras2")
            .linhaDigitavel("linhaDigitavel2")
            .pagadorNome("pagadorNome2")
            .pagadorCpf("pagadorCpf2")
            .pagadorCnpj("pagadorCnpj2");
    }

    public static DepositoBoleto getDepositoBoletoRandomSampleGenerator() {
        return new DepositoBoleto()
            .id(longCount.incrementAndGet())
            .codigoBarras(UUID.randomUUID().toString())
            .linhaDigitavel(UUID.randomUUID().toString())
            .pagadorNome(UUID.randomUUID().toString())
            .pagadorCpf(UUID.randomUUID().toString())
            .pagadorCnpj(UUID.randomUUID().toString());
    }
}
