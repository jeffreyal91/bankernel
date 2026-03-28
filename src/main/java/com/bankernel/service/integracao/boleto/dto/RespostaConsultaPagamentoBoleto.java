package com.bankernel.service.integracao.boleto.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta da consulta de status de um pagamento de boleto.
 */
public record RespostaConsultaPagamentoBoleto(
    /** ID do pagamento no provider. */
    String idPagamento,

    /** Status: CRIADO, EFETIVADO, CANCELADO, REJEITADO, AGENDADO. */
    String status,

    /** Valor pago. */
    BigDecimal valorPago,

    /** Data/hora da efetivação (null se pendente). */
    Instant dataEfetivacao,

    /** Mensagem de erro (null se sucesso). */
    String mensagemErro
) {}
