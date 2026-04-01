package com.bankernel.service;

/**
 * Exceção para erros de contabilidade (conta inativa, tipo operação sem contas, etc.).
 */
public class ContabilidadeException extends RuntimeException {

    private final String codigoErro;

    public ContabilidadeException(String mensagem) {
        super(mensagem);
        this.codigoErro = null;
    }

    public ContabilidadeException(String mensagem, String codigoErro) {
        super(mensagem);
        this.codigoErro = codigoErro;
    }

    public ContabilidadeException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.codigoErro = null;
    }

    public String getCodigoErro() {
        return codigoErro;
    }
}
