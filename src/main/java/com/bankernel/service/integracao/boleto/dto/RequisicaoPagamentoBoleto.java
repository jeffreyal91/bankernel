package com.bankernel.service.integracao.boleto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dados para pagamento de um boleto (saque por boleto).
 * Usado quando o usuário deseja pagar um boleto de terceiro.
 */
public record RequisicaoPagamentoBoleto(
    /** Código de barras do boleto (44 dígitos). */
    String codigoBarras,

    /** Linha digitável (alternativa ao código de barras). */
    String linhaDigitavel,

    /** Valor do pagamento. */
    BigDecimal valor,

    /** Data do pagamento (default: hoje). */
    LocalDate dataPagamento,

    /** Descrição/observação do pagamento. */
    String descricao
) {}
