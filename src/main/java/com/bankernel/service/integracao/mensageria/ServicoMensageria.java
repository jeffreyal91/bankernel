package com.bankernel.service.integracao.mensageria;

/**
 * Interface para serviço de mensageria assíncrona.
 *
 * <p>Abstrai qualquer broker de mensagens (RabbitMQ, Apache Kafka, Amazon SQS,
 * Redis Streams, etc.). O kernel bancário utiliza esta interface para processar
 * operações assíncronas sem acoplamento com um broker específico.</p>
 *
 * <h3>Filas padrão do sistema:</h3>
 * <ul>
 *   <li>{@code deposito.pix} - Processamento de depósitos PIX recebidos</li>
 *   <li>{@code deposito.boleto} - Processamento de depósitos boleto pagos</li>
 *   <li>{@code saque.pix} - Processamento de saques PIX</li>
 *   <li>{@code saque.boleto} - Processamento de pagamentos de boleto</li>
 *   <li>{@code checkout.pagamento} - Processamento de checkouts pagos</li>
 *   <li>{@code notificacao} - Envio de notificações</li>
 *   <li>{@code contabilidade} - Lançamentos contábeis assíncronos</li>
 * </ul>
 *
 * <h3>Responsabilidades da implementação:</h3>
 * <ul>
 *   <li>Conexão e autenticação com o broker</li>
 *   <li>Serialização/deserialização de eventos</li>
 *   <li>Garantia de entrega (at-least-once ou exactly-once)</li>
 *   <li>Dead letter queue para eventos que falharem</li>
 *   <li>Retry com backoff exponencial</li>
 * </ul>
 */
public interface ServicoMensageria {

    /**
     * Publica um evento em uma fila/tópico.
     *
     * @param fila nome da fila (ex: "deposito.pix")
     * @param evento evento de domínio a ser publicado
     * @throws MensageriaException se falhar a publicação
     */
    void publicar(String fila, EventoDominio evento);

    /**
     * Registra um consumidor para processar eventos de uma fila.
     *
     * <p>O consumidor será chamado automaticamente para cada evento
     * recebido na fila especificada.</p>
     *
     * @param fila nome da fila para consumir
     * @param consumidor implementação do processamento
     * @throws MensageriaException se falhar o registro
     */
    void registrarConsumidor(String fila, ConsumidorEvento consumidor);
}
