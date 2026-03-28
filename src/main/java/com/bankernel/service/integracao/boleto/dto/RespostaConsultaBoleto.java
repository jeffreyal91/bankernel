package com.bankernel.service.integracao.boleto.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Resposta da consulta de status de um boleto.
 */
public record RespostaConsultaBoleto(
    /** ID do boleto no provider. */
    String idBoleto,

    /** Status: CRIADO, REGISTRADO, PAGO, CANCELADO, VENCIDO, BAIXADO. */
    String status,

    /** Valor nominal do boleto. */
    BigDecimal valorNominal,

    /** Valor efetivamente pago (null se não pago). */
    BigDecimal valorPago,

    /** Data de vencimento. */
    LocalDate dataVencimento,

    /** Data/hora do pagamento (null se não pago). */
    Instant dataPagamento,

    /** Nosso número. */
    String nossoNumero
) {}
