package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MoedaCarteiraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MoedaCarteira getMoedaCarteiraSample1() {
        return new MoedaCarteira()
            .id(1L)
            .codigo("codigo1")
            .nome("nome1")
            .descricao("descricao1")
            .casasDecimais(1)
            .separadorMilhar("separadorMilhar1")
            .separadorDecimal("separadorDecimal1")
            .simbolo("simbolo1");
    }

    public static MoedaCarteira getMoedaCarteiraSample2() {
        return new MoedaCarteira()
            .id(2L)
            .codigo("codigo2")
            .nome("nome2")
            .descricao("descricao2")
            .casasDecimais(2)
            .separadorMilhar("separadorMilhar2")
            .separadorDecimal("separadorDecimal2")
            .simbolo("simbolo2");
    }

    public static MoedaCarteira getMoedaCarteiraRandomSampleGenerator() {
        return new MoedaCarteira()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .casasDecimais(intCount.incrementAndGet())
            .separadorMilhar(UUID.randomUUID().toString())
            .separadorDecimal(UUID.randomUUID().toString())
            .simbolo(UUID.randomUUID().toString());
    }
}
