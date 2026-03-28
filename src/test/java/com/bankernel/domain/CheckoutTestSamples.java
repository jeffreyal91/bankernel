package com.bankernel.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CheckoutTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Checkout getCheckoutSample1() {
        return new Checkout()
            .id(1L)
            .idProdutoExterno("idProdutoExterno1")
            .nomeProdutoExterno("nomeProdutoExterno1")
            .nomeUsuarioFixo("nomeUsuarioFixo1")
            .tokenCheckout("tokenCheckout1")
            .idExterno("idExterno1");
    }

    public static Checkout getCheckoutSample2() {
        return new Checkout()
            .id(2L)
            .idProdutoExterno("idProdutoExterno2")
            .nomeProdutoExterno("nomeProdutoExterno2")
            .nomeUsuarioFixo("nomeUsuarioFixo2")
            .tokenCheckout("tokenCheckout2")
            .idExterno("idExterno2");
    }

    public static Checkout getCheckoutRandomSampleGenerator() {
        return new Checkout()
            .id(longCount.incrementAndGet())
            .idProdutoExterno(UUID.randomUUID().toString())
            .nomeProdutoExterno(UUID.randomUUID().toString())
            .nomeUsuarioFixo(UUID.randomUUID().toString())
            .tokenCheckout(UUID.randomUUID().toString())
            .idExterno(UUID.randomUUID().toString());
    }
}
