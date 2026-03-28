# Analytics e Inteligência de Dados

## Visão Geral

Módulo planejado para análise de dados transacionais, comportamento de usuários, detecção de anomalias e geração de insights para o negócio.

---

## Dashboards Planejados

### Dashboard Transacional

Visão em tempo real da operação bancária.

| Métrica | Descrição | Granularidade |
|---------|-----------|---------------|
| Volume de transações | Quantidade por tipo (depósito, saque, transferência, cobrança) | Hora, dia, semana, mês |
| Valor transacionado | Soma de valores por tipo de operação | Hora, dia, semana, mês |
| Ticket médio | Valor médio por transação | Dia, semana, mês |
| Taxa de sucesso | % de transações aprovadas vs total | Hora, dia |
| Taxa de rejeição | % de transações rejeitadas com motivos | Dia, semana |
| Pico de uso | Horários e dias com maior volume | Hora do dia, dia da semana |
| Tempo médio de processamento | Latência do fluxo completo | Hora, dia |

### Dashboard de Usuários

Acompanhamento da base de clientes.

| Métrica | Descrição | Granularidade |
|---------|-----------|---------------|
| Novos cadastros | PF e PJ por período | Dia, semana, mês |
| Usuários ativos | Que realizaram ao menos 1 operação | Dia, semana, mês |
| Churn | Usuários que pararam de operar | Semana, mês |
| Distribuição de saldo | Faixas de saldo nas carteiras | Snapshot diário |
| Distribuição geográfica | Usuários por estado/cidade | Mensal |
| Distribuição por plano | Quantos em cada plano de serviço | Mensal |

### Dashboard de Cobranças

Performance dos links de cobrança e recorrência.

| Métrica | Descrição | Granularidade |
|---------|-----------|---------------|
| Cobranças emitidas | Quantidade e valor | Dia, semana, mês |
| Taxa de conversão | % de cobranças pagas vs emitidas | Semana, mês |
| Tempo médio de pagamento | Tempo entre emissão e pagamento | Semana, mês |
| Recorrência ativa | Assinaturas ativas e receita recorrente (MRR) | Mensal |
| Taxa de inadimplência | % de cobranças vencidas não pagas | Mensal |
| Método de pagamento | Distribuição PIX vs Boleto | Mensal |

### Dashboard Contábil

Visão financeira da operação.

| Métrica | Descrição | Granularidade |
|---------|-----------|---------------|
| Saldo total em carteiras | Soma de todos os saldos | Snapshot diário |
| Movimentação por conta contábil | Débitos e créditos por conta | Dia, mês |
| Receita por tipo de operação | Quanto cada operação gera de receita | Mensal |
| Conciliação PIX | Recebidos no provedor vs creditados nas carteiras | Diário |
| Conciliação Boleto | Emitidos vs pagos vs vencidos | Diário |

---

## Detecção de Anomalias

### Regras Baseadas em Limiar

| Anomalia | Regra | Ação |
|----------|-------|------|
| Transação de alto valor | Valor > X vezes a média do usuário | Alerta + revisão manual |
| Frequência incomum | > N transações em Y minutos | Bloqueio temporário + alerta |
| Horário incomum | Operação em horário fora do padrão do usuário | Alerta |
| Destino novo de alto valor | Primeiro saque/transferência para beneficiário + valor alto | Alerta + confirmação |
| Saldo zerado rapidamente | Carteira esvaziada em curto período | Alerta + bloqueio |
| Múltiplos dispositivos | Login de localidades distantes em curto intervalo | Alerta |
| Padrão de fracionamento | Múltiplas transações logo abaixo do limite | Alerta de estruturação |

### Machine Learning (Futuro)

- Modelo de detecção de fraude baseado em padrões históricos
- Clustering de comportamento de usuário para identificar perfis
- Previsão de churn baseado em mudança de comportamento
- Score de risco dinâmico atualizado a cada transação

---

## Relatórios de Exportação

| Formato | Biblioteca | Uso |
|---------|-----------|-----|
| CSV | Java nativo | Exportação simples e rápida |
| Excel (.xlsx) | Apache POI | Relatórios formatados com gráficos |
| PDF | iText / OpenPDF | Extratos e comprovantes |
| JSON | Jackson | Integração com sistemas externos |

### Relatórios Planejados

- **Extrato de conta** - Movimentações de uma carteira em período
- **Relatório de conciliação** - Diferenças entre provedor e sistema
- **Relatório de faturamento** - Receita por período e tipo de operação
- **Relatório regulatório** - Dados para BACEN, COAF, Receita Federal
- **Relatório de inadimplência** - Cobranças vencidas não pagas
- **Relatório de risco** - Distribuição de clientes por nível de risco

---

## Integração com Ferramentas de BI

### Endpoints Otimizados para BI

Endpoints agregados planejados (além do CRUD):

```
GET /api/analytics/transacoes/resumo-diario?dataInicio=2024-01-01&dataFim=2024-01-31
GET /api/analytics/transacoes/por-tipo?periodo=MENSAL
GET /api/analytics/carteiras/distribuicao-saldo
GET /api/analytics/cobrancas/taxa-conversao?periodo=SEMANAL
GET /api/analytics/usuarios/ativos?periodo=MENSAL
GET /api/analytics/conciliacao/pix?data=2024-01-15
```

### Ferramentas Compatíveis

| Ferramenta | Integração | Uso |
|-----------|------------|-----|
| Metabase | Conexão direta ao PostgreSQL | Dashboards interativos |
| Grafana | Prometheus metrics + PostgreSQL | Monitoramento em tempo real |
| Apache Superset | Conexão SQL direta | Análise exploratória |
| Google Data Studio | API REST ou conexão SQL | Relatórios compartilháveis |
| Power BI | ODBC PostgreSQL | Relatórios corporativos |

### Prometheus Metrics (Já Disponível)

O BanKernel já exporta métricas via Micrometer para Prometheus:

```
GET /management/prometheus
```

Métricas incluem: JVM, HTTP requests, cache hits/misses, database pool, custom business metrics.

---

## Eventos para Analytics

O `ServicoMensageria` já suporta publicação de eventos que podem alimentar um pipeline de analytics:

```java
EventoDominio.criar("DEPOSITO_PIX_RECEBIDO", "Deposito", depositoId, Map.of(
    "valor", valor,
    "tipoDeposito", "PIX",
    "tempoProcessamentoMs", duracaoMs
));
```

### Pipeline Planejado

```
Evento de Domínio → Mensageria → Consumidor Analytics → Banco Analítico → Dashboard
```

Opções de banco analítico:
- **PostgreSQL** (mesmo banco, schema separado) - simplicidade
- **ClickHouse** - consultas analíticas de alta performance
- **TimescaleDB** - extensão PostgreSQL para séries temporais
- **Amazon Redshift** - data warehouse na nuvem
