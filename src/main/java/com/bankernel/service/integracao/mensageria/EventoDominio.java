package com.bankernel.service.integracao.mensageria;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Evento de domínio publicado na fila de mensagens.
 *
 * <p>Todos os eventos assíncronos do sistema são representados por esta classe.
 * Cada evento possui um tipo, uma referência à entidade de origem e os dados
 * serializados como mapa chave-valor.</p>
 *
 * <h3>Tipos de evento típicos:</h3>
 * <ul>
 *   <li>{@code DEPOSITO_PIX_RECEBIDO} - PIX recebido via webhook</li>
 *   <li>{@code DEPOSITO_BOLETO_PAGO} - Boleto pago (webhook ou conciliação)</li>
 *   <li>{@code SAQUE_PIX_CONFIRMADO} - Saque PIX confirmado pelo provider</li>
 *   <li>{@code SAQUE_BOLETO_EFETIVADO} - Pagamento de boleto efetivado</li>
 *   <li>{@code TRANSFERENCIA_REALIZADA} - Transferência P2P concluída</li>
 *   <li>{@code CHECKOUT_PAGO} - Checkout de pagamento concluído</li>
 * </ul>
 */
public record EventoDominio(
    /** Identificador único do evento (UUID). */
    String id,

    /** Tipo do evento (ex: "DEPOSITO_PIX_RECEBIDO"). */
    String tipo,

    /** Tipo da entidade de origem (ex: "Deposito", "Saque"). */
    String tipoEntidade,

    /** ID da entidade de origem. */
    Long idEntidade,

    /** Dados adicionais do evento. */
    Map<String, Object> dados,

    /** Data/hora de criação do evento. */
    Instant criadoEm
) {
    /**
     * Cria um novo evento com ID e timestamp gerados automaticamente.
     */
    public static EventoDominio criar(String tipo, String tipoEntidade, Long idEntidade, Map<String, Object> dados) {
        return new EventoDominio(
            UUID.randomUUID().toString(),
            tipo,
            tipoEntidade,
            idEntidade,
            dados,
            Instant.now()
        );
    }
}
