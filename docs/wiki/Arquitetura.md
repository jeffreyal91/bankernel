# Arquitetura

## Visão Geral

O BanKernel segue uma arquitetura em camadas com separação clara de responsabilidades:

```
┌─────────────────────────────────────────────────┐
│                 REST Controllers                 │  ← Entrada HTTP (42 controllers)
│              (web/rest/*.java)                   │
├─────────────────────────────────────────────────┤
│              Service Layer                       │  ← Lógica de negócio
│         (service/*Service.java)                  │
│         (service/*QueryService.java)             │  ← Filtros dinâmicos
├─────────────────────────────────────────────────┤
│           DTO / Mapper Layer                     │  ← Transformação de dados
│    (service/dto/*.java + mapper/*.java)           │     MapStruct (compile-time)
├─────────────────────────────────────────────────┤
│            Repository Layer                      │  ← Acesso a dados
│        (repository/*.java)                       │     Spring Data JPA
├─────────────────────────────────────────────────┤
│             Domain Layer                         │  ← Entidades JPA (38)
│       (domain/*.java + enumeration/*.java)       │     Enums (52)
├─────────────────────────────────────────────────┤
│              Integração                          │  ← Interfaces Strategy Pattern
│     (service/integracao/pix|boleto|...)          │     Sem implementação de provedor
├─────────────────────────────────────────────────┤
│            Infraestrutura                        │
│  PostgreSQL │ EhCache │ JWT │ Liquibase          │
└─────────────────────────────────────────────────┘
```

## Princípios

### BigDecimal para Dinheiro
Todos os valores monetários usam `BigDecimal(19,4)` — 19 dígitos de precisão com 4 casas decimais. Nunca `Double` ou `Float`, que causam erros de arredondamento em operações financeiras.

### Referência Polimórfica
A entidade `Transacao` não tem 25+ relações diretas com cada tipo de operação. Em vez disso, usa dois campos:
- `tipoEntidadeOrigem` (String): nome da entidade (`"Deposito"`, `"Saque"`, `"Transferencia"`)
- `idEntidadeOrigem` (Long): ID da entidade

Isso elimina o problema de god-entity e permite adicionar novos tipos de operação sem modificar a transação.

### Idempotência
Toda operação financeira possui um `numeroReferencia` único. Se a mesma operação for enviada duas vezes (retry, falha de rede), o sistema identifica pela referência e não processa duplicada.

### Partida Dobrada (Double-Entry Bookkeeping)
Todo movimento de saldo gera automaticamente um lançamento contábil com débito e crédito em contas contábeis tipadas (ativo, passivo, receita, despesa).

### Strategy Pattern para Integrações
Nenhum código de provedor específico existe no core. As integrações PIX, Boleto, Mensageria e Notificação são interfaces que cada provedor implementa. Isso permite trocar de provedor sem modificar o core.

## Fluxo de uma Operação Financeira

```
1. Controller recebe requisição HTTP
2. Validação de dados (Jakarta Validation)
3. Service verifica saldo e regras de negócio
4. Cria entidade (Deposito/Saque/Transferencia/Cobranca)
5. Cria Transacao com referência polimórfica
6. Atualiza saldo da Carteira (BigDecimal)
7. Cria HistoricoOperacao
8. Cria LancamentoContabil (débito + crédito)
9. Publica evento na mensageria (assíncrono)
10. Retorna DTO ao cliente
```

## Cache

EhCache com 30+ entidades cacheadas:
- TTL: 1 hora (configurável)
- Max entries: 100 (dev) / 1.000 (prod)
- Cache de segundo nível Hibernate habilitado
- Cache de usuários por login e email

## Segurança

- JWT Bearer Token stateless
- BCrypt para hash de senhas
- Roles: `ROLE_USER`, `ROLE_ADMIN`
- CORS configurável por profile
- Auditoria automática em todas as entidades (quem criou, quando, quem modificou)
