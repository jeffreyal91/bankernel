package com.bankernel.service.integracao.pix.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta do envio de um pagamento PIX (por chave ou manual).
 */
public record RespostaEnvioPix(
    /** End-to-end ID único do pagamento PIX. */
    String endToEndId,

    /** ID do pagamento no provider. */
    String idPagamento,

    /** Status: INICIADO, PROCESSADO, CONFIRMADO, REJEITADO. */
    String status,

    /** Valor efetivamente enviado. */
    BigDecimal valorEnviado,

    /** Data/hora da operação. */
    Instant dataOperacao,

    /** Dados do recebedor (preenchidos após confirmação). */
    DadosRecebedor recebedor,

    /** Mensagem de erro (null se sucesso). */
    String mensagemErro
) {
    /**
     * Dados do recebedor retornados pelo SPI após a transferência.
     */
    public record DadosRecebedor(
        /** Nome do recebedor. */
        String nome,
        /** CPF do recebedor. */
        String cpf,
        /** CNPJ do recebedor. */
        String cnpj,
        /** ISPB da instituição do recebedor. */
        String ispb,
        /** Nome da instituição do recebedor. */
        String nomeBanco,
        /** Agência. */
        String agencia,
        /** Número da conta. */
        String numeroConta,
        /** Tipo da conta: CORRENTE, POUPANCA, SALARIO, TRANSACIONAL. */
        String tipoConta
    ) {}
}
