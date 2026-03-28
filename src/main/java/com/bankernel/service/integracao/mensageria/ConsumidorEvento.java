package com.bankernel.service.integracao.mensageria;

/**
 * Interface funcional para consumidores de eventos de domínio.
 *
 * <p>Implementar esta interface para processar eventos recebidos de uma fila.
 * O consumidor é responsável pelo tratamento de erros e pela garantia de
 * idempotência no processamento.</p>
 */
@FunctionalInterface
public interface ConsumidorEvento {

    /**
     * Processa um evento recebido da fila.
     *
     * <p>A implementação deve ser idempotente - processar o mesmo evento
     * múltiplas vezes não deve causar efeitos colaterais indesejados.</p>
     *
     * @param evento evento de domínio recebido
     * @throws Exception se o processamento falhar (o evento pode ser reenfileirado)
     */
    void processar(EventoDominio evento) throws Exception;
}
