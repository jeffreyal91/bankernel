package com.bankernel.service.integracao.pix.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dados para criação de QR Code PIX dinâmico (cobrança imediata).
 * QR dinâmico é de uso único com valor definido.
 */
public record RequisicaoPixDinamico(
    /** Chave PIX do recebedor. */
    String chavePix,

    /** Valor da cobrança (obrigatório para dinâmico). */
    BigDecimal valor,

    /** Tempo de expiração em segundos. */
    Integer expiracaoSegundos,

    /** CPF do devedor (11 dígitos, sem máscara). */
    String devedorCpf,

    /** CNPJ do devedor (14 dígitos, sem máscara). */
    String devedorCnpj,

    /** Nome completo do devedor. */
    String devedorNome,

    /** Descrição/solicitação ao pagador. */
    String descricao,

    /** Informações adicionais (chave-valor exibidas ao pagador). */
    List<InfoAdicional> informacoesAdicionais
) {
    /**
     * Par chave-valor de informação adicional exibida ao pagador.
     */
    public record InfoAdicional(String nome, String valor) {}
}
