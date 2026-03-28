package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PessoaFisicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PessoaFisica getPessoaFisicaSample1() {
        return new PessoaFisica()
            .id(1L)
            .cpf("cpf1")
            .nomeCompleto("nomeCompleto1")
            .nomeSocial("nomeSocial1")
            .nomeMae("nomeMae1")
            .telefone("telefone1");
    }

    public static PessoaFisica getPessoaFisicaSample2() {
        return new PessoaFisica()
            .id(2L)
            .cpf("cpf2")
            .nomeCompleto("nomeCompleto2")
            .nomeSocial("nomeSocial2")
            .nomeMae("nomeMae2")
            .telefone("telefone2");
    }

    public static PessoaFisica getPessoaFisicaRandomSampleGenerator() {
        return new PessoaFisica()
            .id(longCount.incrementAndGet())
            .cpf(UUID.randomUUID().toString())
            .nomeCompleto(UUID.randomUUID().toString())
            .nomeSocial(UUID.randomUUID().toString())
            .nomeMae(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString());
    }
}
