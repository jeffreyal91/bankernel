package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Documento getDocumentoSample1() {
        return new Documento().id(1L).nome("nome1").tipoArquivo("tipoArquivo1").endereco("endereco1").tamanho(1L);
    }

    public static Documento getDocumentoSample2() {
        return new Documento().id(2L).nome("nome2").tipoArquivo("tipoArquivo2").endereco("endereco2").tamanho(2L);
    }

    public static Documento getDocumentoRandomSampleGenerator() {
        return new Documento()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .tipoArquivo(UUID.randomUUID().toString())
            .endereco(UUID.randomUUID().toString())
            .tamanho(longCount.incrementAndGet());
    }
}
