<p align="center">
  <h1 align="center">BanKernel</h1>
  <p align="center">Kernel bancário digital open-source para o mercado brasileiro</p>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21_LTS-orange?logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring_Boot-3.4-green?logo=springboot" alt="Spring Boot 3">
  <img src="https://img.shields.io/badge/JHipster-8.11-blue?logo=jhipster" alt="JHipster 8">
  <img src="https://img.shields.io/badge/PostgreSQL-15+-blue?logo=postgresql" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/API-REST-red" alt="REST API">
  <img src="https://img.shields.io/badge/Idioma-Portugu%C3%AAs_BR-green" alt="Português">
</p>

---

## O que é o BanKernel?

BanKernel é um **backend de core bancário** pronto para produção, projetado para fintechs, bancos digitais e plataformas de pagamento que operam no Brasil. Ele fornece toda a infraestrutura transacional necessária para gerenciar carteiras digitais, processar depósitos e saques via PIX e Boleto, realizar transferências internas e emitir cobranças - tudo com contabilidade de partida dobrada integrada.

O projeto é **100% backend** (sem frontend), com **todas as entidades, campos e enums em português brasileiro**, e foi desenhado para ser agnóstico de provider: as integrações com PIX e Boleto são interfaces documentadas que cada instituição implementa conforme sua API.

### Para quem serve?

- **Fintechs** que precisam de um core bancário para lançar carteiras digitais
- **Bancos digitais** que buscam uma base sólida para construir seus produtos
- **Plataformas de pagamento** que necessitam de gestão transacional completa
- **Desenvolvedores** que querem entender como funciona um sistema bancário por dentro
- **Startups** que precisam de MVP rápido com arquitetura escalável

---

## Funcionalidades

### Gestão de Pessoas (PF e PJ)

- Cadastro completo de **Pessoa Física** (CPF, dados pessoais, nome da mãe, profissão)
- Cadastro completo de **Pessoa Jurídica** (CNPJ, razão social, CNAE, regime tributário, capital social)
- Sistema de **endereços** múltiplos por usuário (residencial, comercial, correspondência)
- **Colaboradores PJ** com permissões granulares por carteira (visualizar, operar, administrar)
- Níveis de risco configuráveis (baixo, médio, alto, crítico)
- Planos de serviço vinculáveis a PF e PJ

### Carteiras Digitais

- **Multi-moeda**: cada usuário pode ter carteiras em diferentes moedas (BRL, USD, EUR, GBP)
- Saldo com **BigDecimal** (precisão de 19 dígitos, 4 casas decimais) - sem erros de arredondamento
- **Valor congelado** para transações pendentes (reserva de saldo)
- Limite negativo configurável por carteira
- Número de conta gerado automaticamente (único)

### Depósitos

- **PIX**: QR Code estático, dinâmico imediato e dinâmico com vencimento
  - Suporte a juros, multa, desconto e abatimento (regulamentação BACEN)
  - Rastreamento por `identificadorTransacao` (txId) e `identificadorPontaAPonta` (endToEndId)
  - Dados do pagador capturados automaticamente (nome, CPF/CNPJ)
- **Boleto**: emissão com código de barras e linha digitável
  - Suporte a vencimento, multa, juros e desconto
  - Rastreamento por referência externa

### Saques

- **PIX por chave**: CPF, CNPJ, email, telefone ou chave aleatória
- **PIX manual**: envio direto para conta bancária (ISPB, agência, conta)
- **PIX por QR Code**: leitura e pagamento de QR
- **Boleto**: pagamento de boletos de terceiros
- **TED**: transferência eletrônica
- Registro de contas bancárias favoritas (com dados completos: titular, banco, agência, conta, ISPB, SWIFT)

### Transferências Internas (P2P)

- Transferência entre carteiras por chave interna (email, CPF, telefone ou número de conta)
- Rastreabilidade completa (origem, destino, referência)
- Idempotência garantida por `numeroReferencia`

### Cobranças e Pagamentos

- **Links de cobrança** configuráveis com:
  - URL de retorno para pagamento aprovado, cancelado e rejeitado
  - Desconto (fixo ou percentual)
  - Valor mínimo de pagamento
  - Período de validade (data início e fim)
  - Identificador externo para integração com ERP
- **Cobranças unitárias** (PIX ou Boleto) com rastreamento completo
- **Recorrência**: planos com intervalo, parcelas, dias de teste e tentativas de cobrança
- **Assinaturas**: dados completos do devedor, múltiplos métodos de pagamento

### Contabilidade (Partida Dobrada)

- **Plano de contas contábil** com categorias: ativo, passivo, receita, despesa
- **Lançamentos contábeis** automáticos: todo movimento financeiro gera débito + crédito
- Saldo de contas contábeis atualizado em tempo real
- Categorização: operacional, financeira, administrativa

### Configuração e Controle

- **Planos de serviço** com flag de plano padrão
- **Configurações do sistema** chave-valor tipadas (String, Integer, Boolean, BigDecimal) por módulo
- **Bloqueio de operações** por dia da semana e faixa horária (manual ou automático)
- **Cadastro de feriados** para controle de dias úteis

### Registro de Integrações

- Log completo de todas as chamadas a provedores externos
- Campos: fornecedor, tipo, operação, corpo da requisição/resposta, código HTTP, duração em milissegundos
- Referência polimórfica à entidade de origem
- Útil para debugging, auditoria e conciliação

### Notificações

- Sistema multi-canal: email, push, SMS, interna
- Controle de situação (pendente, enviada, falha, lida)
- Registro de canal utilizado

---

## API REST - O que cada endpoint oferece

Cada uma das **38 entidades** expõe automaticamente **7 endpoints REST**:

| Verbo | Endpoint | Descrição |
|-------|---------|-----------|
| `POST` | `/api/{entidade}` | Criar registro (com validação) |
| `GET` | `/api/{entidade}` | Listar com filtros, paginação e ordenação |
| `GET` | `/api/{entidade}/count` | Contar registros (com filtros) |
| `GET` | `/api/{entidade}/{id}` | Buscar por ID |
| `PUT` | `/api/{entidade}/{id}` | Atualizar registro completo |
| `PATCH` | `/api/{entidade}/{id}` | Atualizar parcialmente (`merge-patch+json`) |
| `DELETE` | `/api/{entidade}/{id}` | Remover registro |

**Total: ~266 endpoints REST** gerados automaticamente.

### Filtros Avançados (14 entidades com filtros)

As entidades principais possuem **filtros dinâmicos via query params** baseados em JPA Criteria:

```
GET /api/transacaos?valorEnviado.greaterThan=100&situacao.equals=APROVADA&tipoPagamento.in=PIX,BOLETO&ativa.equals=true
GET /api/depositos?tipoDeposito.equals=PIX&situacaoDeposito.equals=PENDENTE&valor.greaterThanOrEqual=50
GET /api/pessoas-fisicas?nivelRisco.equals=ALTO&situacao.notEquals=BLOQUEADO
GET /api/carteiras?saldo.greaterThan=0&ativa.equals=true&sort=saldo,desc
```

**Tipos de filtro disponíveis:**

| Tipo | Operações | Exemplo |
|------|-----------|---------|
| `StringFilter` | equals, contains, startsWith, endsWith, in | `?nome.contains=Silva` |
| `LongFilter` | equals, greaterThan, lessThan, in, between | `?id.greaterThan=100` |
| `BigDecimalFilter` | equals, greaterThan, lessThan, between | `?valor.greaterThanOrEqual=50.00` |
| `BooleanFilter` | equals, specified | `?ativa.equals=true` |
| `EnumFilter` | equals, in, notEquals | `?situacao.in=PENDENTE,APROVADA` |
| `InstantFilter` | equals, greaterThan, lessThan, between | `?criadoEm.greaterThan=2024-01-01T00:00:00Z` |
| **Relação** | Filtro pelo ID da entidade relacionada | `?carteiraId.equals=5` |

### Paginação e Ordenação

Todas as listagens suportam paginação e ordenação multi-campo:

```
GET /api/depositos?page=0&size=20&sort=valor,desc&sort=criadoEm,asc
```

**Cabeçalhos de resposta:**
- `X-Total-Count`: total de registros que atendem ao filtro
- `Link`: links de navegação (first, prev, next, last)

### Atualização Parcial (PATCH)

Suporte a `merge-patch+json` para atualizar apenas os campos enviados:

```bash
curl -X PATCH /api/depositos/123 \
  -H "Content-Type: application/merge-patch+json" \
  -d '{"situacaoDeposito": "APROVADO", "contabilizado": true}'
```

---

## Segurança

| Recurso | Implementação |
|---------|--------------|
| Autenticação | JWT Bearer Token (OAuth2 Resource Server) |
| Senhas | BCrypt (hash) |
| Sessões | Stateless (sem estado no servidor) |
| Autorização | Role-based (`ROLE_USER`, `ROLE_ADMIN`) |
| Método | `@Secured` por endpoint |
| CORS | Configurável por profile (habilitado em dev, desabilitado em prod) |

**Endpoints públicos:** `/api/authenticate`, `/api/register`, `/api/activate`, `/management/health`
**Endpoints admin:** `/api/admin/**`, `/management/**`, `/v3/api-docs/**`
**Demais endpoints:** Requerem autenticação

---

## Cache e Performance

- **EhCache** como cache de segundo nível do Hibernate
- **30+ entidades** cacheadas com TTL de 1 hora
- Cache de usuários por login e por email
- Batch size de 25 para otimização de queries N+1
- Lazy loading com detecção de collection fetch
- HTTP/2 habilitado
- Compressão de resposta (min 1024 bytes)

---

## Auditoria Automática

Todas as entidades possuem campos de auditoria preenchidos automaticamente:

| Campo | Descrição | Atualização |
|-------|-----------|-------------|
| `createdBy` | Usuário que criou | Apenas na criação |
| `createdDate` | Data/hora de criação | Apenas na criação |
| `lastModifiedBy` | Último usuário que alterou | A cada alteração |
| `lastModifiedDate` | Data/hora da última alteração | A cada alteração |

O auditor é extraído automaticamente do contexto de segurança JWT.

---

## Monitoramento

- **Actuator endpoints**: health, info, metrics, loggers, caches, liquibase
- **Prometheus**: exportação de métricas nativa
- **Health checks**: liveness, readiness, banco de dados
- **Thread dump** e acesso a logs via API

---

## Integrações (Strategy Pattern)

Nenhuma implementação de provedor está incluída. Todas as integrações são **interfaces documentadas com DTOs** prontas para serem implementadas.

### PIX - `ServicoPixIntegracao`

8 métodos com DTOs completos (records Java imutáveis):

| Método | Request DTO | Response DTO |
|--------|-------------|-------------|
| `criarPixEstatico()` | `RequisicaoPixEstatico` | `RespostaPixQrCode` |
| `criarPixDinamicoImediato()` | `RequisicaoPixDinamico` | `RespostaPixQrCode` |
| `criarPixDinamicoVencimento()` | `RequisicaoPixDinamicoVencimento` | `RespostaPixQrCode` |
| `consultarCobranca(txId)` | - | `RespostaConsultaPix` |
| `enviarPixPorChave()` | `RequisicaoEnvioPixChave` | `RespostaEnvioPix` |
| `enviarPixManual()` | `RequisicaoEnvioPixManual` | `RespostaEnvioPix` |
| `solicitarDevolucao()` | - | `RespostaDevolucaoPix` |
| `configurarWebhook()` | - | - |

PIX com vencimento suporta: juros (valor/dia ou % mês), multa (fixa ou %), desconto (fixo ou % até data), abatimento.

### Boleto - `ServicoBoletoIntegracao`

5 métodos com DTOs FEBRABAN-compliant:

| Método | Request DTO | Response DTO |
|--------|-------------|-------------|
| `gerarBoleto()` | `RequisicaoBoleto` | `RespostaBoleto` |
| `consultarBoleto(id)` | - | `RespostaConsultaBoleto` |
| `cancelarBoleto(id)` | - | `boolean` |
| `pagarBoleto()` | `RequisicaoPagamentoBoleto` | `RespostaPagamentoBoleto` |
| `consultarPagamento(id)` | - | `RespostaConsultaPagamentoBoleto` |

`RequisicaoBoleto` inclui: sacado (endereço completo), multa, juros, desconto, mensagens (4 linhas), sacador avalista.

### Mensageria - `ServicoMensageria`

Acoplável a qualquer broker (RabbitMQ, Kafka, SQS):

```java
void publicar(String fila, EventoDominio evento);
void registrarConsumidor(String fila, ConsumidorEvento consumidor);
```

`EventoDominio` inclui: tipo, entidade de origem, dados como mapa, timestamp. Factory method `EventoDominio.criar()` para conveniência.

Filas padrão sugeridas: `deposito.pix`, `deposito.boleto`, `saque.pix`, `saque.boleto`, `cobranca.pagamento`, `notificacao`, `contabilidade`.

### Notificação - `ServicoNotificacao`

```java
void enviarEmail(String destinatario, String assunto, String corpoHtml);
void enviarPush(String tokenDispositivo, String titulo, String mensagem);
void enviarSms(String telefone, String mensagem);
```

### Como implementar um provedor

```java
@Service
@Profile("meu-provedor")
public class MeuProvedorPixService implements ServicoPixIntegracao {

    @Override
    public RespostaPixQrCode criarPixDinamicoImediato(RequisicaoPixDinamico requisicao) {
        // 1. Autenticar com a API do provedor
        // 2. Serializar requisição conforme API
        // 3. Fazer chamada HTTP
        // 4. Deserializar resposta
        // 5. Retornar RespostaPixQrCode
    }

    // ... demais métodos
}
```

---

## Stack Técnica

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Java | 21 LTS | Linguagem base |
| Spring Boot | 3.4.x | Framework principal |
| Spring Security | 6.x | Autenticação e autorização JWT |
| Spring Data JPA | 3.x | Acesso a dados com Hibernate |
| JHipster | 8.11 | Geração de código e scaffolding |
| PostgreSQL | 15+ | Banco de dados relacional |
| Liquibase | 4.x | Migrações de banco (65 changesets) |
| MapStruct | 1.6.x | Mapeamento entidade/DTO em compile-time |
| EhCache | 3.x | Cache de segundo nível JPA |
| SpringDoc OpenAPI | 2.8.x | Documentação Swagger automática |
| Jackson | 2.17.x | Serialização/deserialização JSON |
| Micrometer | 1.x | Métricas e monitoramento Prometheus |
| JUnit 5 | 5.x | Framework de testes |
| Mockito | 5.x | Mocking para testes |

---

## Executar

### Pré-requisitos

- Java 21+
- PostgreSQL 15+
- Maven 3.9+

### Desenvolvimento

```bash
# Criar banco
createdb bankernel

# Executar (aplica migrações automaticamente)
./mvnw
```

- API: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Health: `http://localhost:8080/management/health`

### Produção

```bash
./mvnw -Pprod clean verify
java -jar target/*.jar
```

### Docker

```bash
npm run java:docker
docker compose -f src/main/docker/app.yml up -d
```

### Configuração do Banco

`src/main/resources/config/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bankernel
    username: bankernel
    password: bankernel
```

---

## Estrutura do Projeto

```
bankernel/
├── bankernel.jdl                    # Definição de todas as entidades (JDL)
├── CLAUDE.md                        # Guia para uso com Claude Code
├── CONTRIBUTING.md                  # Guia de contribuição
├── pom.xml                          # Dependências Maven
└── src/
    ├── main/
    │   ├── java/com/bankernel/
    │   │   ├── domain/              # 38 entidades JPA
    │   │   │   └── enumeration/     # 52 enums
    │   │   ├── repository/          # 38 repositórios Spring Data
    │   │   ├── service/
    │   │   │   ├── dto/             # 38 DTOs
    │   │   │   ├── mapper/          # 38 mappers MapStruct
    │   │   │   ├── criteria/        # 14 classes de filtro
    │   │   │   └── integracao/      # Interfaces de integração
    │   │   │       ├── pix/         # 1 interface + 9 DTOs
    │   │   │       ├── boleto/      # 1 interface + 6 DTOs
    │   │   │       ├── mensageria/  # 1 interface + 2 classes
    │   │   │       └── notificacao/ # 1 interface + 1 exceção
    │   │   └── web/rest/            # 42 controllers REST
    │   └── resources/
    │       ├── config/
    │       │   ├── application*.yml # Configurações por profile
    │       │   └── liquibase/       # 65 migrações de banco
    │       └── i18n/                # Internacionalização (pt-br, en)
    └── test/                        # 42 classes de teste de integração
```

## Regenerar Entidades

O arquivo `bankernel.jdl` é a fonte de verdade para todas as entidades:

```bash
jhipster jdl bankernel.jdl --force
```

## Documentação Completa

A documentação detalhada está em [`docs/wiki/`](docs/wiki/):

| Página | Conteúdo |
|--------|----------|
| [Home](docs/wiki/Home.md) | Índice geral |
| [Arquitetura](docs/wiki/Arquitetura.md) | Princípios, camadas, fluxos |
| [Módulos e Entidades](docs/wiki/Modulos-e-Entidades.md) | Todas as 38 entidades detalhadas |
| [API REST](docs/wiki/API-REST.md) | 266 endpoints documentados |
| [Filtros e Consultas](docs/wiki/Filtros-e-Consultas.md) | Guia completo de filtros com exemplos |
| [Autenticação JWT](docs/wiki/Autenticacao-JWT.md) | Login, tokens, roles |
| [Primeiros Passos](docs/wiki/Primeiros-Passos.md) | Setup e primeira execução |
| [Implementando Provedor PIX](docs/wiki/Implementando-Provedor-PIX.md) | Guia passo a passo |
| [Implementando Provedor Boleto](docs/wiki/Implementando-Provedor-Boleto.md) | Guia passo a passo |
| [Configurando Mensageria](docs/wiki/Configurando-Mensageria.md) | RabbitMQ, Kafka, SQS |
| [Roadmap](docs/wiki/Roadmap.md) | 8 fases planejadas |
| [Integrações Bancárias](docs/wiki/Integracoes-Bancarias.md) | BB, Itaú, Bradesco, Caixa, Santander |
| [Segurança e Identidade](docs/wiki/Seguranca-e-Identidade.md) | KYC, OCR, face match, bureaus |
| [Analytics](docs/wiki/Analytics.md) | Dashboards, anomalias, BI |

## Roadmap

- **Fase 1** (Atual): Core bancário com 38 entidades, API REST, interfaces de integração
- **Fase 2**: Integrações com os 5 maiores bancos do Brasil (BB, Itaú, Bradesco, Caixa, Santander)
- **Fase 3**: Segurança e validação de identidade (KYC, OCR, face match, PEP, sanções)
- **Fase 4**: Mensageria (RabbitMQ, Kafka, SQS) e processamento assíncrono
- **Fase 5**: Analytics, detecção de anomalias e integração com ferramentas de BI
- **Fase 6**: Notificações avançadas (templates, preferências, agendamento)
- **Fase 7**: Infraestrutura (Docker, Kubernetes, CI/CD, Terraform)
- **Fase 8**: Multi-tenancy, webhooks, SDK, sandbox, Open Finance

## Contribuindo

Veja [CONTRIBUTING.md](CONTRIBUTING.md) para o guia completo.
