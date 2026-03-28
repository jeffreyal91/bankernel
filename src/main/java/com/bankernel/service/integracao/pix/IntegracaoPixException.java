package com.bankernel.service.integracao.pix;

/**
 * Exceção lançada quando ocorre falha na integração com o serviço PIX.
 *
 * <p>Encapsula erros de comunicação, autenticação, validação e
 * respostas inesperadas do provider PIX.</p>
 */
public class IntegracaoPixException extends RuntimeException {

    private final String codigoErro;
    private final Integer statusHttp;

    public IntegracaoPixException(String mensagem) {
        super(mensagem);
        this.codigoErro = null;
        this.statusHttp = null;
    }

    public IntegracaoPixException(String mensagem, String codigoErro, Integer statusHttp) {
        super(mensagem);
        this.codigoErro = codigoErro;
        this.statusHttp = statusHttp;
    }

    public IntegracaoPixException(String mensagem, Throwable causa) {
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
