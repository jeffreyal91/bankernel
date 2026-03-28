package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LinkCobrancaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LinkCobranca getLinkCobrancaSample1() {
        return new LinkCobranca()
            .id(1L)
            .nome("nome1")
            .chaveAcesso("chaveAcesso1")
            .chaveApi("chaveApi1")
            .urlRetorno("urlRetorno1")
            .urlPagamentoAprovado("urlPagamentoAprovado1")
            .urlPagamentoCancelado("urlPagamentoCancelado1")
            .urlPagamentoRejeitado("urlPagamentoRejeitado1")
            .identificadorExterno("identificadorExterno1")
            .numeroReferencia("numeroReferencia1");
    }

    public static LinkCobranca getLinkCobrancaSample2() {
        return new LinkCobranca()
            .id(2L)
            .nome("nome2")
            .chaveAcesso("chaveAcesso2")
            .chaveApi("chaveApi2")
            .urlRetorno("urlRetorno2")
            .urlPagamentoAprovado("urlPagamentoAprovado2")
            .urlPagamentoCancelado("urlPagamentoCancelado2")
            .urlPagamentoRejeitado("urlPagamentoRejeitado2")
            .identificadorExterno("identificadorExterno2")
            .numeroReferencia("numeroReferencia2");
    }

    public static LinkCobranca getLinkCobrancaRandomSampleGenerator() {
        return new LinkCobranca()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .chaveAcesso(UUID.randomUUID().toString())
            .chaveApi(UUID.randomUUID().toString())
            .urlRetorno(UUID.randomUUID().toString())
            .urlPagamentoAprovado(UUID.randomUUID().toString())
            .urlPagamentoCancelado(UUID.randomUUID().toString())
            .urlPagamentoRejeitado(UUID.randomUUID().toString())
            .identificadorExterno(UUID.randomUUID().toString())
            .numeroReferencia(UUID.randomUUID().toString());
    }
}
