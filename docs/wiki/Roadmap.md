# Roadmap do Projeto

## Fase 1 - Core Bancário (Atual)

- [x] Entidades de domínio (38 entidades, 52 enums)
- [x] API REST completa (266+ endpoints)
- [x] Filtros avançados (14 entidades com JPA Criteria)
- [x] DTOs e MapStruct
- [x] Autenticação JWT
- [x] Cache EhCache
- [x] Auditoria automática
- [x] Interfaces de integração PIX (8 métodos + 9 DTOs)
- [x] Interfaces de integração Boleto (5 métodos + 6 DTOs)
- [x] Interface de mensageria (acoplável)
- [x] Interface de notificação (email, push, SMS)
- [x] Contabilidade partida dobrada
- [x] Documentação Swagger/OpenAPI

## Fase 2 - Integrações Bancárias

> Implementações concretas dos provedores PIX e Boleto para os principais bancos do Brasil.

- [ ] **Banco do Brasil** - PIX API + Cobrança + Boleto registrado
- [ ] **Itaú Unibanco** - PIX Itaú + Boleto Itaú (API Itaú Empresas)
- [ ] **Bradesco** - PIX Bradesco + Boleto registrado (API Bradesco Net Empresa)
- [ ] **Caixa Econômica Federal** - PIX Caixa + Boleto SIGCB
- [ ] **Santander** - PIX Santander + Boleto registrado (API Santander)

Cada integração será um **módulo separado** (Maven module ou Spring profile), seguindo a interface `ServicoPixIntegracao` e `ServicoBoletoIntegracao`.

Veja detalhes em [Integrações Bancárias](Integracoes-Bancarias).

## Fase 3 - Segurança e Validação de Identidade

> Módulos para KYC (Know Your Customer), validação de documentos e prevenção a fraudes.

- [ ] **Validação de CPF/CNPJ** - Consulta situação cadastral na Receita Federal
- [ ] **Validação de identidade** - OCR de documentos (RG, CNH, passaporte)
- [ ] **Reconhecimento facial** - Liveness detection e face match
- [ ] **Consulta de dados pessoais** - Integração com bureaus (Serasa, SPC, Boa Vista)
- [ ] **PEP (Pessoa Exposta Politicamente)** - Consulta em listas oficiais
- [ ] **Sanções e listas restritivas** - OFAC, UE, ONU
- [ ] **Análise de risco** - Score baseado em regras configuráveis
- [ ] **Background check empresarial** - Consulta sócios, quadro societário, processos

Veja detalhes em [Segurança e Validação de Identidade](Seguranca-e-Identidade).

## Fase 4 - Mensageria e Processamento Assíncrono

> Implementações concretas do `ServicoMensageria` para diferentes brokers.

- [ ] **RabbitMQ** - Implementação com Spring AMQP
- [ ] **Apache Kafka** - Implementação com Spring Kafka
- [ ] **Amazon SQS** - Implementação com AWS SDK
- [ ] **Redis Streams** - Implementação com Spring Data Redis
- [ ] **Dead Letter Queue** - Tratamento de eventos que falharam
- [ ] **Retry com backoff exponencial** - Política de re-tentativas configurável
- [ ] **Event sourcing** - Registro imutável de todos os eventos de domínio

## Fase 5 - Analytics e Inteligência de Dados

> Módulo de análise de dados para acompanhar uso, identificar melhorias e detectar anomalias.

- [ ] **Dashboard transacional** - Volume, valor médio, pico de uso por hora/dia
- [ ] **Análise de comportamento** - Padrões de uso por tipo de operação
- [ ] **Detecção de anomalias** - Transações fora do padrão (valor, frequência, horário)
- [ ] **Relatórios de conciliação** - PIX recebidos vs creditados, boletos emitidos vs pagos
- [ ] **Métricas de negócio** - Receita por tipo de operação, churn de usuários, ticket médio
- [ ] **Exportação de dados** - CSV, Excel (Apache POI), PDF
- [ ] **Integração com BI** - Endpoints otimizados para ferramentas como Metabase, Grafana

Veja detalhes em [Analytics e Inteligência de Dados](Analytics).

## Fase 6 - Notificações Avançadas

> Implementações concretas e recursos avançados de notificação.

- [ ] **Amazon SES** - Envio de emails via AWS
- [ ] **SendGrid** - Alternativa de email
- [ ] **Firebase Cloud Messaging** - Push notifications Android/iOS
- [ ] **Twilio** - SMS e WhatsApp
- [ ] **Templates de notificação** - Templates HTML/texto configuráveis por tipo de evento
- [ ] **Preferências do usuário** - Quais canais cada usuário quer receber
- [ ] **Agendamento** - Envio programado de notificações
- [ ] **Digest** - Resumo periódico de atividades

## Fase 7 - Infraestrutura e DevOps

- [ ] **Docker Compose completo** - App + PostgreSQL + broker + cache
- [ ] **Kubernetes Helm Chart** - Deploy em clusters K8s
- [ ] **CI/CD com GitHub Actions** - Build, test, deploy automático
- [ ] **Terraform** - Infraestrutura como código (AWS/GCP/Azure)
- [ ] **Observabilidade** - OpenTelemetry + Jaeger para tracing distribuído
- [ ] **Rate limiting** - Limitação de requisições por API key/usuário
- [ ] **API Gateway** - Kong ou Spring Cloud Gateway

## Fase 8 - Recursos Avançados

- [ ] **Multi-tenancy** - Múltiplas instituições no mesmo deployment
- [ ] **Webhooks outbound** - Notificação de eventos para sistemas externos
- [ ] **SDK para clientes** - Bibliotecas Java, Python, Node.js para consumir a API
- [ ] **Gestão de APIs (API Keys)** - Criação e revogação de chaves por merchant
- [ ] **Sandbox** - Ambiente de testes com dados fake e simulação de provedores
- [ ] **Migração de dados** - Ferramentas para importar dados de outros sistemas
- [ ] **TED/DOC** - Integração com SPB para transferências interbancárias
- [ ] **Open Finance** - Compartilhamento de dados conforme regulamentação BACEN
