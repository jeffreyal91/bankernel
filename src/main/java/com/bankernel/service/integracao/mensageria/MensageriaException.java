package com.bankernel.service.integracao.mensageria;

/**
 * Exceção lançada quando ocorre falha no serviço de mensageria.
 */
public class MensageriaException extends RuntimeException {

    public MensageriaException(String mensagem) {
        super(mensagem);
    }

    public MensageriaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
