package com.bankernel.service.integracao.boleto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dados para geração de boleto bancário de cobrança.
 * Segue as especificações FEBRABAN para boletos registrados.
 */
public record RequisicaoBoleto(
    /** Valor nominal do boleto. */
    BigDecimal valorNominal,

    /** Data de vencimento. */
    LocalDate dataVencimento,

    /** Dados do sacado (pagador). */
    Sacado sacado,

    /** Configuração de multa por atraso (null se não aplicável). */
    Multa multa,

    /** Configuração de juros por atraso (null se não aplicável). */
    Juros juros,

    /** Configuração de desconto por antecipação (null se não aplicável). */
    Desconto desconto,

    /** Mensagens impressas no corpo do boleto (até 4 linhas). */
    Mensagens mensagens,

    /** Dados do sacador avalista (null se não aplicável). */
    SacadorAvalista sacadorAvalista,

    /** Número do documento (seu número) para controle do emitente. */
    String seuNumero,

    /** Espécie do documento (ex: "DM" duplicata mercantil, "DS" duplicata serviço). */
    String especieDocumento,

    /** Se o sacado aceita o boleto (true = aceite). */
    Boolean aceite
) {
    /**
     * Dados do sacado (quem paga o boleto).
     */
    public record Sacado(
        /** Nome completo ou razão social. */
        String nome,
        /** CPF (11 dígitos) ou CNPJ (14 dígitos) sem máscara. */
        String documento,
        /** Tipo: PESSOA_FISICA ou PESSOA_JURIDICA. */
        String tipoPessoa,
        /** Email para envio do boleto. */
        String email,
        /** Telefone de contato. */
        String telefone,
        /** CEP (8 dígitos). */
        String cep,
        /** UF (2 caracteres). */
        String estado,
        /** Cidade. */
        String cidade,
        /** Bairro. */
        String bairro,
        /** Logradouro (rua/avenida). */
        String logradouro,
        /** Número. */
        String numero,
        /** Complemento. */
        String complemento
    ) {}

    /**
     * Configuração de multa aplicada após vencimento.
     *
     * @param tipo FIXO (valor absoluto) ou PERCENTUAL (sobre valor nominal)
     * @param valor Valor ou percentual da multa
     * @param dataInicio Data a partir da qual a multa é aplicada (geralmente dia seguinte ao vencimento)
     */
    public record Multa(String tipo, BigDecimal valor, LocalDate dataInicio) {}

    /**
     * Configuração de juros aplicados após vencimento.
     *
     * @param tipo VALOR_DIA (valor fixo por dia), TAXA_MENSAL (percentual ao mês)
     * @param valor Valor ou taxa dos juros
     */
    public record Juros(String tipo, BigDecimal valor) {}

    /**
     * Configuração de desconto por pagamento antecipado.
     *
     * @param tipo FIXO_ATE_DATA ou PERCENTUAL_ATE_DATA
     * @param valor Valor ou percentual do desconto
     * @param dataLimite Data limite para aplicação do desconto
     */
    public record Desconto(String tipo, BigDecimal valor, LocalDate dataLimite) {}

    /**
     * Mensagens impressas no corpo do boleto (máximo 4 linhas).
     */
    public record Mensagens(
        String linha1,
        String linha2,
        String linha3,
        String linha4
    ) {}

    /**
     * Dados do sacador avalista (garantidor da dívida).
     */
    public record SacadorAvalista(
        String nome,
        String documento,
        String tipoPessoa,
        String cep,
        String estado,
        String cidade,
        String bairro,
        String logradouro,
        String numero,
        String complemento
    ) {}
}
