package com.bankernel.service.integracao.boleto;

/**
 * Exceção lançada quando ocorre falha na integração com o serviço de Boletos.
 */
public class IntegracaoBoletoException extends RuntimeException {

    private final String codigoErro;
    private final Integer statusHttp;

    public IntegracaoBoletoException(String mensagem) {
        super(mensagem);
        this.codigoErro = null;
        this.statusHttp = null;
    }

    public IntegracaoBoletoException(String mensagem, String codigoErro, Integer statusHttp) {
        super(mensagem);
        this.codigoErro = codigoErro;
        this.statusHttp = statusHttp;
    }

    public IntegracaoBoletoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.codigoErro = null;
        this.statusHttp = null;
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    public Integer getStatusHttp() {
        return statusHttp;
    }
}
