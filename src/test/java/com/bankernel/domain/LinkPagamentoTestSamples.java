package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LinkPagamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LinkPagamento getLinkPagamentoSample1() {
        return new LinkPagamento()
            .id(1L)
            .nome("nome1")
            .token("token1")
            .chave("chave1")
            .webhookUrl("webhookUrl1")
            .urlPagamentoAprovado("urlPagamentoAprovado1")
            .urlPagamentoCancelado("urlPagamentoCancelado1")
            .urlPagamentoRejeitado("urlPagamentoRejeitado1")
            .idExterno("idExterno1")
            .numeroReferencia("numeroReferencia1");
    }

    public static LinkPagamento getLinkPagamentoSample2() {
        return new LinkPagamento()
            .id(2L)
            .nome("nome2")
            .token("token2")
            .chave("chave2")
            .webhookUrl("webhookUrl2")
            .urlPagamentoAprovado("urlPagamentoAprovado2")
            .urlPagamentoCancelado("urlPagamentoCancelado2")
            .urlPagamentoRejeitado("urlPagamentoRejeitado2")
            .idExterno("idExterno2")
            .numeroReferencia("numeroReferencia2");
    }

    public static LinkPagamento getLinkPagamentoRandomSampleGenerator() {
        return new LinkPagamento()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .token(UUID.randomUUID().toString())
            .chave(UUID.randomUUID().toString())
            .webhookUrl(UUID.randomUUID().toString())
            .urlPagamentoAprovado(UUID.randomUUID().toString())
            .urlPagamentoCancelado(UUID.randomUUID().toString())
            .urlPagamentoRejeitado(UUID.randomUUID().toString())
            .idExterno(UUID.randomUUID().toString())
            .numeroReferencia(UUID.randomUUID().toString());
    }
}
