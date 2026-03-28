package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RegistroIntegracaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static RegistroIntegracao getRegistroIntegracaoSample1() {
        return new RegistroIntegracao()
            .id(1L)
            .fornecedor("fornecedor1")
            .operacao("operacao1")
            .codigoHttp(1)
            .mensagemErro("mensagemErro1")
            .duracaoMilissegundos(1L)
            .tipoEntidadeOrigem("tipoEntidadeOrigem1")
            .idEntidadeOrigem(1L);
    }

    public static RegistroIntegracao getRegistroIntegracaoSample2() {
        return new RegistroIntegracao()
            .id(2L)
            .fornecedor("fornecedor2")
            .operacao("operacao2")
            .codigoHttp(2)
            .mensagemErro("mensagemErro2")
            .duracaoMilissegundos(2L)
            .tipoEntidadeOrigem("tipoEntidadeOrigem2")
            .idEntidadeOrigem(2L);
    }

    public static RegistroIntegracao getRegistroIntegracaoRandomSampleGenerator() {
        return new RegistroIntegracao()
            .id(longCount.incrementAndGet())
            .fornecedor(UUID.randomUUID().toString())
            .operacao(UUID.randomUUID().toString())
            .codigoHttp(intCount.incrementAndGet())
            .mensagemErro(UUID.randomUUID().toString())
            .duracaoMilissegundos(longCount.incrementAndGet())
            .tipoEntidadeOrigem(UUID.randomUUID().toString())
            .idEntidadeOrigem(longCount.incrementAndGet());
    }
}
