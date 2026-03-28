package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssinaturaRecorrenciaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssinaturaRecorrencia getAssinaturaRecorrenciaSample1() {
        return new AssinaturaRecorrencia()
            .id(1L)
            .numeroOrdem("numeroOrdem1")
            .numeroReferencia("numeroReferencia1")
            .devedorEmail("devedorEmail1")
            .devedorDocumento("devedorDocumento1")
            .devedorNome("devedorNome1")
            .devedorTelefone("devedorTelefone1")
            .devedorCep("devedorCep1")
            .devedorUf("devedorUf1")
            .devedorCidade("devedorCidade1")
            .devedorBairro("devedorBairro1")
            .devedorEndereco("devedorEndereco1")
            .idProdutoExterno("idProdutoExterno1")
            .identificadorExterno("identificadorExterno1")
            .nomeProdutoExterno("nomeProdutoExterno1");
    }

    public static AssinaturaRecorrencia getAssinaturaRecorrenciaSample2() {
        return new AssinaturaRecorrencia()
            .id(2L)
            .numeroOrdem("numeroOrdem2")
            .numeroReferencia("numeroReferencia2")
            .devedorEmail("devedorEmail2")
            .devedorDocumento("devedorDocumento2")
            .devedorNome("devedorNome2")
            .devedorTelefone("devedorTelefone2")
            .devedorCep("devedorCep2")
            .devedorUf("devedorUf2")
            .devedorCidade("devedorCidade2")
            .devedorBairro("devedorBairro2")
            .devedorEndereco("devedorEndereco2")
            .idProdutoExterno("idProdutoExterno2")
            .identificadorExterno("identificadorExterno2")
            .nomeProdutoExterno("nomeProdutoExterno2");
    }

    public static AssinaturaRecorrencia getAssinaturaRecorrenciaRandomSampleGenerator() {
        return new AssinaturaRecorrencia()
            .id(longCount.incrementAndGet())
            .numeroOrdem(UUID.randomUUID().toString())
            .numeroReferencia(UUID.randomUUID().toString())
            .devedorEmail(UUID.randomUUID().toString())
            .devedorDocumento(UUID.randomUUID().toString())
            .devedorNome(UUID.randomUUID().toString())
            .devedorTelefone(UUID.randomUUID().toString())
            .devedorCep(UUID.randomUUID().toString())
            .devedorUf(UUID.randomUUID().toString())
            .devedorCidade(UUID.randomUUID().toString())
            .devedorBairro(UUID.randomUUID().toString())
            .devedorEndereco(UUID.randomUUID().toString())
            .idProdutoExterno(UUID.randomUUID().toString())
            .identificadorExterno(UUID.randomUUID().toString())
            .nomeProdutoExterno(UUID.randomUUID().toString());
    }
}
