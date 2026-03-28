package com.bankernel.service.integracao.pix.dto;

import java.math.BigDecimal;

/**
 * Dados para criação de QR Code PIX estático.
 * QR estático pode ser reutilizado para múltiplos pagamentos.
 */
public record RequisicaoPixEstatico(
    /** Chave PIX do recebedor (CPF, CNPJ, email, telefone ou chave aleatória). */
    String chavePix,

    /** Valor fixo do PIX (opcional - se null, pagador define o valor). */
    BigDecimal valor,

    /** Nome do recebedor (até 25 caracteres conforme padrão PIX). */
    String nomeRecebedor,

    /** Cidade do recebedor (até 15 caracteres conforme padrão PIX). */
    String cidadeRecebedor,

    /** Campo livre para informações adicionais. */
    String campoLivre,

    /** Identificador da transação (até 25 caracteres, sem espaços). */
    String txId
) {}
