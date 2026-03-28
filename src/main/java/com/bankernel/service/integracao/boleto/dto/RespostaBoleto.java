package com.bankernel.service.integracao.boleto.dto;

import java.time.Instant;

/**
 * Resposta da geração de um boleto bancário.
 */
public record RespostaBoleto(
    /** ID do boleto no provider. */
    String idBoleto,

    /** Código de barras (44 dígitos). */
    String codigoBarras,

    /** Linha digitável (47 dígitos formatados). */
    String linhaDigitavel,

    /** Nosso número (identificador do boleto no banco). */
    String nossoNumero,

    /** URL para download do PDF do boleto. */
    String urlPdf,

    /** Status do boleto: CRIADO, REGISTRADO. */
    String status,

    /** Data/hora de criação. */
    Instant criadoEm,

    /** Mensagem de erro (null se sucesso). */
    String mensagemErro
) {}
