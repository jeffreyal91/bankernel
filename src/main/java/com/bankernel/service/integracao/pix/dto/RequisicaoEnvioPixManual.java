package com.bankernel.service.integracao.pix.dto;

import java.math.BigDecimal;

/**
 * Dados para envio de pagamento PIX manual (dados bancários completos).
 * Usado quando não se tem a chave PIX, apenas os dados bancários.
 */
public record RequisicaoEnvioPixManual(
    /** ISPB da instituição destino (8 dígitos). */
    String ispbDestino,

    /** Agência da conta destino (sem dígito verificador). */
    String agencia,

    /** Número da conta destino (com dígito verificador). */
    String numeroConta,

    /** Tipo da conta: CORRENTE, POUPANCA, SALARIO, TRANSACIONAL. */
    String tipoConta,

    /** CPF do destinatário (11 dígitos, sem máscara). */
    String cpfDestinatario,

    /** CNPJ do destinatário (14 dígitos, sem máscara). */
    String cnpjDestinatario,

    /** Nome completo do destinatário. */
    String nomeDestinatario,

    /** Valor do pagamento. */
    BigDecimal valor,

    /** Descrição/informação do pagador. */
    String descricao,

    /** Identificador único da transação para idempotência. */
    String idTransacao
) {}
