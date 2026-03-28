package com.bankernel.service.integracao.pix.dto;

import java.math.BigDecimal;

/**
 * Dados para envio de pagamento PIX por chave.
 * Usado para saques e transferências externas.
 */
public record RequisicaoEnvioPixChave(
    /** Chave PIX do destinatário. */
    String chaveDestino,

    /** Tipo da chave: CPF, CNPJ, EMAIL, TELEFONE, CHAVE_ALEATORIA. */
    String tipoChave,

    /** Valor do pagamento. */
    BigDecimal valor,

    /** Descrição/informação do pagador (até 140 caracteres). */
    String descricao,

    /** Identificador único da transação para idempotência. */
    String idTransacao
) {}
