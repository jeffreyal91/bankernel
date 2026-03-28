package com.bankernel.service.integracao.pix.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta da solicitação de devolução de um PIX recebido.
 */
public record RespostaDevolucaoPix(
    /** ID da devolução gerado pelo provider. */
    String idDevolucao,

    /** End-to-end ID do PIX original. */
    String endToEndIdOriginal,

    /** Valor devolvido. */
    BigDecimal valorDevolvido,

    /** Status: EM_PROCESSAMENTO, DEVOLVIDO, NAO_REALIZADO. */
    String status,

    /** Motivo da devolução. */
    String motivo,

    /** Data/hora da devolução. */
    Instant dataDevolucao,

    /** Mensagem de erro (null se sucesso). */
    String mensagemErro
) {}
