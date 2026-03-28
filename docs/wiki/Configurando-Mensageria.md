# Configurando Mensageria

## Visão Geral

O `ServicoMensageria` é a interface que abstrai qualquer broker de mensagens. Implemente para RabbitMQ, Kafka, SQS ou o broker de sua escolha.

## Filas Padrão

| Fila | Evento | Descrição |
|------|--------|-----------|
| `deposito.pix` | `DEPOSITO_PIX_RECEBIDO` | PIX recebido via webhook |
| `deposito.boleto` | `DEPOSITO_BOLETO_PAGO` | Boleto pago |
| `saque.pix` | `SAQUE_PIX_CONFIRMADO` | Saque PIX confirmado |
| `saque.boleto` | `SAQUE_BOLETO_EFETIVADO` | Pagamento de boleto efetivado |
| `cobranca.pagamento` | `COBRANCA_PAGA` | Cobrança paga |
| `notificacao` | `NOTIFICACAO_PENDENTE` | Notificação para enviar |
| `contabilidade` | `LANCAMENTO_PENDENTE` | Lançamento contábil pendente |

## Exemplo: RabbitMQ

```java
@Service
@Profile("rabbitmq")
public class RabbitMqMensageriaService implements ServicoMensageria {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publicar(String fila, EventoDominio evento) {
        try {
            String json = objectMapper.writeValueAsString(evento);
            rabbitTemplate.convertAndSend(fila, json);
        } catch (Exception e) {
            throw new MensageriaException("Falha ao publicar evento na fila " + fila, e);
        }
    }

    @Override
    public void registrarConsumidor(String fila, ConsumidorEvento consumidor) {
        // Configurar via @RabbitListener ou programaticamente
    }
}
```

## Exemplo: Kafka

```java
@Service
@Profile("kafka")
public class KafkaMensageriaService implements ServicoMensageria {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publicar(String fila, EventoDominio evento) {
        kafkaTemplate.send(fila, evento.id(), serialize(evento));
    }
}
```

## Publicando Eventos

```java
@Service
public class DepositoProcessService {

    private final ServicoMensageria mensageria;

    public void processarPixRecebido(Long depositoId, BigDecimal valor) {
        var evento = EventoDominio.criar(
            "DEPOSITO_PIX_RECEBIDO",
            "Deposito",
            depositoId,
            Map.of("valor", valor, "tipo", "PIX")
        );
        mensageria.publicar("deposito.pix", evento);
    }
}
```

## Consumindo Eventos

```java
@Service
public class DepositoPixConsumidor implements ConsumidorEvento {

    @Override
    public void processar(EventoDominio evento) throws Exception {
        Long depositoId = evento.idEntidade();
        // 1. Buscar depósito no banco
        // 2. Atualizar situação para APROVADO
        // 3. Creditar saldo na carteira
        // 4. Criar lançamento contábil
        // 5. Enviar notificação ao usuário
    }
}
```
