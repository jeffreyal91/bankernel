package com.bankernel.service.integracao.pix.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Dados para criação de QR Code PIX dinâmico com vencimento.
 * Suporta juros, multa, desconto e abatimento conforme regulamentação BACEN.
 */
public record RequisicaoPixDinamicoVencimento(
    /** Chave PIX do recebedor. */
    String chavePix,

    /** Valor nominal da cobrança. */
    BigDecimal valorNominal,

    /** Data de vencimento da cobrança. */
    LocalDate dataVencimento,

    /**
     * Dias após vencimento em que o QR Code ainda pode ser pago.
     * Se 0, vence no dia do vencimento.
     */
    Integer diasAposVencimento,

    /** CPF do devedor (11 dígitos). */
    String devedorCpf,

    /** CNPJ do devedor (14 dígitos). */
    String devedorCnpj,

    /** Nome completo do devedor. */
    String devedorNome,

    /** Descrição/solicitação ao pagador. */
    String descricao,

    /** Configuração de juros (null se não há juros). */
    Juros juros,

    /** Configuração de multa (null se não há multa). */
    Multa multa,

    /** Configuração de desconto (null se não há desconto). */
    Desconto desconto,

    /** Valor de abatimento (dedução sobre o valor nominal). */
    BigDecimal abatimento,

    /** Informações adicionais. */
    List<RequisicaoPixDinamico.InfoAdicional> informacoesAdicionais
) {
    /**
     * Configuração de juros após vencimento.
     *
     * @param tipo VALOR_DIA (valor fixo por dia) ou PERCENTUAL_MES (taxa mensal)
     * @param valor Valor do juros conforme o tipo
     */
    public record Juros(TipoValor tipo, BigDecimal valor) {}

    /**
     * Configuração de multa por atraso.
     *
     * @param tipo FIXO (valor absoluto) ou PERCENTUAL (sobre valor nominal)
     * @param valor Valor da multa conforme o tipo
     */
    public record Multa(TipoValor tipo, BigDecimal valor) {}

    /**
     * Configuração de desconto por antecipação.
     *
     * @param tipo FIXO_ATE_DATA ou PERCENTUAL_ATE_DATA
     * @param valor Valor do desconto conforme o tipo
     * @param dataLimite Data limite para aplicação do desconto
     */
    public record Desconto(TipoValor tipo, BigDecimal valor, LocalDate dataLimite) {}

    /** Tipo de cálculo para juros, multa e desconto. */
    public enum TipoValor {
        FIXO,
        PERCENTUAL,
        VALOR_DIA,
        PERCENTUAL_MES,
        FIXO_ATE_DATA,
        PERCENTUAL_ATE_DATA
    }
}
