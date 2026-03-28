package com.bankernel.service.integracao.boleto.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta do pagamento de um boleto.
 */
public record RespostaPagamentoBoleto(
    /** ID do pagamento no provider. */
    String idPagamento,

    /** Status: CRIADO, EFETIVADO, CANCELADO, REJEITADO, AGENDADO. */
    String status,

    /** Valor efetivamente pago. */
    BigDecimal valorPago,

    /** Data/hora da operação. */
    Instant dataOperacao,

    /** Mensagem de erro (null se sucesso). */
    String mensagemErro
) {}
