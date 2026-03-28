package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepositoPixTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DepositoPix getDepositoPixSample1() {
        return new DepositoPix()
            .id(1L)
            .codigoQr("codigoQr1")
            .identificadorTransacao("identificadorTransacao1")
            .identificadorPontaAPonta("identificadorPontaAPonta1")
            .pagadorNome("pagadorNome1")
            .pagadorCpf("pagadorCpf1")
            .pagadorCnpj("pagadorCnpj1");
    }

    public static DepositoPix getDepositoPixSample2() {
        return new DepositoPix()
            .id(2L)
            .codigoQr("codigoQr2")
            .identificadorTransacao("identificadorTransacao2")
            .identificadorPontaAPonta("identificadorPontaAPonta2")
            .pagadorNome("pagadorNome2")
            .pagadorCpf("pagadorCpf2")
            .pagadorCnpj("pagadorCnpj2");
    }

    public static DepositoPix getDepositoPixRandomSampleGenerator() {
        return new DepositoPix()
            .id(longCount.incrementAndGet())
            .codigoQr(UUID.randomUUID().toString())
            .identificadorTransacao(UUID.randomUUID().toString())
            .identificadorPontaAPonta(UUID.randomUUID().toString())
            .pagadorNome(UUID.randomUUID().toString())
            .pagadorCpf(UUID.randomUUID().toString())
            .pagadorCnpj(UUID.randomUUID().toString());
    }
}
