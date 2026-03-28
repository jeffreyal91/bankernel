package com.bankernel.service.integracao.pix.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta da consulta de status de uma cobrança PIX.
 */
public record RespostaConsultaPix(
    /** Identificador da transação PIX. */
    String txId,

    /** Status atual: ATIVA, CONCLUIDA, REMOVIDA_PSP, REMOVIDA_USUARIO. */
    String status,

    /** Valor original da cobrança. */
    BigDecimal valorOriginal,

    /** Valor efetivamente pago (null se não pago). */
    BigDecimal valorPago,

    /** End-to-end ID do pagamento (preenchido quando pago). */
    String endToEndId,

    /** Data/hora do pagamento (null se não pago). */
    Instant dataPagamento,

    /** Nome do pagador (preenchido quando pago). */
    String pagadorNome,

    /** CPF do pagador. */
    String pagadorCpf,

    /** CNPJ do pagador. */
    String pagadorCnpj,

    /** Informação adicional do pagador. */
    String infoPagador
) {}
