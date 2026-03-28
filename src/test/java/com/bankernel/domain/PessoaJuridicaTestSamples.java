package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PessoaJuridicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PessoaJuridica getPessoaJuridicaSample1() {
        return new PessoaJuridica()
            .id(1L)
            .cnpj("cnpj1")
            .razaoSocial("razaoSocial1")
            .nomeFantasia("nomeFantasia1")
            .telefone("telefone1")
            .sitioWeb("sitioWeb1")
            .descricao("descricao1")
            .regimeTributario("regimeTributario1")
            .codigoNaturezaJuridica("codigoNaturezaJuridica1")
            .atividadePrincipal("atividadePrincipal1")
            .cpfRepresentanteLegal("cpfRepresentanteLegal1")
            .numeroRegistro("numeroRegistro1");
    }

    public static PessoaJuridica getPessoaJuridicaSample2() {
        return new PessoaJuridica()
            .id(2L)
            .cnpj("cnpj2")
            .razaoSocial("razaoSocial2")
            .nomeFantasia("nomeFantasia2")
            .telefone("telefone2")
            .sitioWeb("sitioWeb2")
            .descricao("descricao2")
            .regimeTributario("regimeTributario2")
            .codigoNaturezaJuridica("codigoNaturezaJuridica2")
            .atividadePrincipal("atividadePrincipal2")
            .cpfRepresentanteLegal("cpfRepresentanteLegal2")
            .numeroRegistro("numeroRegistro2");
    }

    public static PessoaJuridica getPessoaJuridicaRandomSampleGenerator() {
        return new PessoaJuridica()
            .id(longCount.incrementAndGet())
            .cnpj(UUID.randomUUID().toString())
            .razaoSocial(UUID.randomUUID().toString())
            .nomeFantasia(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .sitioWeb(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .regimeTributario(UUID.randomUUID().toString())
            .codigoNaturezaJuridica(UUID.randomUUID().toString())
            .atividadePrincipal(UUID.randomUUID().toString())
            .cpfRepresentanteLegal(UUID.randomUUID().toString())
            .numeroRegistro(UUID.randomUUID().toString());
    }
}
