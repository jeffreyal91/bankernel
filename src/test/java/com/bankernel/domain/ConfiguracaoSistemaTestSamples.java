package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConfiguracaoSistemaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConfiguracaoSistema getConfiguracaoSistemaSample1() {
        return new ConfiguracaoSistema().id(1L).chave("chave1").valor("valor1").descricao("descricao1").modulo("modulo1");
    }

    public static ConfiguracaoSistema getConfiguracaoSistemaSample2() {
        return new ConfiguracaoSistema().id(2L).chave("chave2").valor("valor2").descricao("descricao2").modulo("modulo2");
    }

    public static ConfiguracaoSistema getConfiguracaoSistemaRandomSampleGenerator() {
        return new ConfiguracaoSistema()
            .id(longCount.incrementAndGet())
            .chave(UUID.randomUUID().toString())
            .valor(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .modulo(UUID.randomUUID().toString());
    }
}
