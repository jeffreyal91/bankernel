# Referência da API REST

## Endpoints por Módulo

Cada entidade expõe 7 operações REST. Total: **~266 endpoints**.

### Pessoa (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/pessoa-fisicas` | Gestão de pessoas físicas (PF) |
| CRUD (7) | `/api/pessoa-juridicas` | Gestão de pessoas jurídicas (PJ) |
| CRUD (7) | `/api/enderecos` | Endereços de usuários |
| CRUD (7) | `/api/colaborador-pjs` | Colaboradores de PJ |
| CRUD (7) | `/api/permissao-colaboradors` | Permissões de colaboradores |

### Carteira (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/moedas` | Moedas disponíveis (BRL, USD, EUR) |
| CRUD (7) | `/api/moeda-carteiras` | Configuração de moedas nas carteiras |
| CRUD (7) | `/api/carteiras` | Carteiras digitais dos usuários |

### Movimentação (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/transacaos` | Transações (core transacional) |
| CRUD (7) | `/api/tipo-operacaos` | Tipos de operação |
| CRUD (7) | `/api/historico-operacaos` | Histórico de operações |

### Depósito (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/depositos` | Depósitos |
| CRUD (7) | `/api/deposito-pixes` | Detalhes PIX dos depósitos |
| CRUD (7) | `/api/deposito-boletos` | Detalhes Boleto dos depósitos |

### Saque (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/saques` | Saques |
| CRUD (7) | `/api/saque-pixes` | Detalhes PIX dos saques |
| CRUD (7) | `/api/saque-boletos` | Detalhes Boleto dos saques |
| CRUD (7) | `/api/conta-bancarias` | Contas bancárias para saques |

### Transferência (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/transferencias` | Transferências P2P internas |

### Cobrança (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/link-cobrancas` | Links de cobrança |
| CRUD (7) | `/api/cobrancas` | Cobranças individuais |
| CRUD (7) | `/api/plano-recorrencias` | Planos de recorrência |
| CRUD (7) | `/api/assinatura-recorrencias` | Assinaturas |

### Contabilidade (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/conta-contabils` | Contas contábeis |
| CRUD (7) | `/api/lancamento-contabils` | Lançamentos contábeis |

### Configuração (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/planos` | Planos de serviço |
| CRUD (7) | `/api/configuracao-sistemas` | Configurações chave-valor |
| CRUD (7) | `/api/bloqueio-operacaos` | Bloqueios de operação |
| CRUD (7) | `/api/feriados` | Feriados |

### Gestão (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/escritorios` | Escritórios |
| CRUD (7) | `/api/administradors` | Administradores |

### Notificação (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/notificacaos` | Notificações |

### Integração (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/registro-integracaos` | Logs de integração |

### Referência (`/api/`)

| Método | Endpoint | Descrição |
|--------|---------|-----------|
| CRUD (7) | `/api/pais` | Países |
| CRUD (7) | `/api/profissaos` | Profissões |
| CRUD (7) | `/api/tipo-negocios` | Tipos de negócio |
| CRUD (7) | `/api/banco-referencias` | Bancos (código, ISPB) |
| CRUD (7) | `/api/documentos` | Documentos |

---

## Operações CRUD Padrão

Cada endpoint "CRUD (7)" implementa:

| Verbo | Rota | Resposta | Descrição |
|-------|------|---------|-----------|
| `POST` | `/api/{entidade}` | `201 Created` | Criar novo registro |
| `GET` | `/api/{entidade}` | `200 OK` + headers de paginação | Listar com filtros |
| `GET` | `/api/{entidade}/count` | `200 OK` (número) | Contar registros filtrados |
| `GET` | `/api/{entidade}/{id}` | `200 OK` ou `404` | Buscar por ID |
| `PUT` | `/api/{entidade}/{id}` | `200 OK` | Atualizar completo |
| `PATCH` | `/api/{entidade}/{id}` | `200 OK` ou `404` | Atualizar parcial (merge-patch) |
| `DELETE` | `/api/{entidade}/{id}` | `204 No Content` | Remover |

## Headers de Resposta (Paginação)

```
X-Total-Count: 1523
Link: </api/transacaos?page=0&size=20>; rel="first",
      </api/transacaos?page=1&size=20>; rel="prev",
      </api/transacaos?page=3&size=20>; rel="next",
      </api/transacaos?page=76&size=20>; rel="last"
```

## Autenticação

Todos os endpoints (exceto `/api/authenticate`, `/api/register` e `/management/health`) requerem JWT Bearer Token:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## Swagger UI

Documentação interativa disponível em:

```
http://localhost:8080/swagger-ui/index.html
```

Requer login com `ROLE_ADMIN` para acessar.
