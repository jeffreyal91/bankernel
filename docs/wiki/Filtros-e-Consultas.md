# Filtros e Consultas Avançadas

## Entidades com Filtros

14 entidades possuem filtros dinâmicos via JPA Criteria:

`Transacao`, `Deposito`, `Saque`, `Transferencia`, `Cobranca`, `HistoricoOperacao`, `PessoaFisica`, `PessoaJuridica`, `Carteira`, `ContaBancaria`, `RegistroIntegracao`, `Notificacao`, `LancamentoContabil`, `ContaContabil`

## Operadores por Tipo de Campo

### Campos de Texto (StringFilter)

```
?campo.equals=valor              # Exatamente igual
?campo.notEquals=valor           # Diferente
?campo.contains=valor            # Contém (LIKE %valor%)
?campo.doesNotContain=valor      # Não contém
?campo.in=val1,val2,val3         # Está na lista
?campo.notIn=val1,val2           # Não está na lista
?campo.specified=true            # Campo preenchido (NOT NULL)
?campo.specified=false           # Campo vazio (IS NULL)
```

### Campos Numéricos (LongFilter, BigDecimalFilter)

```
?campo.equals=100                # Igual a
?campo.notEquals=100             # Diferente de
?campo.greaterThan=100           # Maior que (>)
?campo.greaterThanOrEqual=100    # Maior ou igual (>=)
?campo.lessThan=100              # Menor que (<)
?campo.lessThanOrEqual=100       # Menor ou igual (<=)
?campo.in=100,200,300            # Está na lista
?campo.notIn=100,200             # Não está na lista
?campo.specified=true            # Campo preenchido
```

### Campos Booleanos (BooleanFilter)

```
?campo.equals=true               # Verdadeiro
?campo.equals=false              # Falso
?campo.specified=true            # Campo preenchido
```

### Campos Enum (EnumFilter)

```
?campo.equals=APROVADA           # Igual ao valor do enum
?campo.notEquals=CANCELADA       # Diferente
?campo.in=PENDENTE,APROVADA      # Está na lista
?campo.notIn=CANCELADA,REJEITADA # Não está na lista
?campo.specified=true            # Campo preenchido
```

### Campos de Data/Hora (InstantFilter)

```
?campo.equals=2024-01-15T10:30:00Z
?campo.greaterThan=2024-01-01T00:00:00Z
?campo.lessThan=2024-12-31T23:59:59Z
?campo.greaterThanOrEqual=2024-01-01T00:00:00Z
?campo.lessThanOrEqual=2024-06-30T23:59:59Z
?campo.specified=true
```

### Filtro por Relacionamento

Filtra pela ID da entidade relacionada:

```
?carteiraId.equals=5             # Transações da carteira 5
?usuarioId.equals=10             # Registros do usuário 10
?moedaCarteiraId.in=1,2          # Registros em moedas 1 ou 2
```

## Paginação

```
?page=0                          # Página (0-based)
?size=20                         # Itens por página (default: 20)
?sort=campo,asc                  # Ordenar ascendente
?sort=campo,desc                 # Ordenar descendente
?sort=campo1,desc&sort=campo2,asc  # Multi-campo
```

## Exemplos Práticos

### Transações PIX aprovadas acima de R$ 100 no último mês
```
GET /api/transacaos?tipoPagamento.equals=PIX&situacao.equals=APROVADA&valorEnviado.greaterThan=100&createdDate.greaterThan=2024-02-01T00:00:00Z&sort=createdDate,desc
```

### Depósitos PIX pendentes de uma carteira específica
```
GET /api/depositos?tipoDeposito.equals=PIX&situacaoDeposito.equals=PENDENTE&carteiraId.equals=42&sort=valor,desc
```

### Pessoas físicas de alto risco ativas
```
GET /api/pessoa-fisicas?nivelRisco.equals=ALTO&situacao.equals=ATIVO&sort=createdDate,desc&size=50
```

### Carteiras com saldo positivo ordenadas por saldo
```
GET /api/carteiras?saldo.greaterThan=0&ativa.equals=true&sort=saldo,desc
```

### Saques do mês com valor entre R$ 500 e R$ 5000
```
GET /api/saques?valorSaque.greaterThanOrEqual=500&valorSaque.lessThanOrEqual=5000&createdDate.greaterThan=2024-03-01T00:00:00Z
```

### Cobranças não pagas (vencidas)
```
GET /api/cobrancas?situacao.in=PENDENTE,EXPIRADO&contabilizado.equals=false
```

### Contar transações rejeitadas hoje
```
GET /api/transacaos/count?situacao.equals=REJEITADA&createdDate.greaterThan=2024-03-15T00:00:00Z
```

### Registros de integração com falha
```
GET /api/registro-integracaos?sucesso.equals=false&tipoIntegracao.equals=PIX&sort=createdDate,desc&size=100
```

## Combinando com Paginação

```
GET /api/transacaos?tipoPagamento.equals=PIX&situacao.equals=APROVADA&page=2&size=50&sort=valorEnviado,desc&sort=createdDate,asc
```

Resposta inclui headers:
- `X-Total-Count: 1523` — total de registros que atendem ao filtro
- `Link: <...>; rel="first", <...>; rel="next", <...>; rel="last"` — navegação
