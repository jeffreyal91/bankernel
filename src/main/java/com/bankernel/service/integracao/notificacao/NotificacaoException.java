package com.bankernel.service.integracao.notificacao;

/**
 * Exceção lançada quando ocorre falha no envio de notificações.
 */
public class NotificacaoException extends RuntimeException {

    private final String canal;

    public NotificacaoException(String mensagem, String canal) {
        super(mensagem);
        this.canal = canal;
    }

    public NotificacaoException(String mensagem, String canal, Throwable causa) {
        super(mensagem, causa);
        this.canal = canal;
    }

    public String getCanal() {
        return canal;
    }
}
