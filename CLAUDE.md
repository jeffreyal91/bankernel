# BanKernel - Guia para Claude Code

## Sobre o Projeto

BanKernel é um kernel bancário digital backend-only. JHipster 8.x, Spring Boot 3, Java 21. Entidades, campos e enums em português brasileiro.

## Comandos

```bash
./mvnw compile -DskipTests    # Compilar
./mvnw test                    # Testes
./mvnw                         # Executar (requer PostgreSQL)
jhipster jdl bankernel.jdl --force  # Regenerar entidades
```

## Estrutura de Pacotes

```
com.bankernel.domain/             → Entidades JPA (NÃO editar - usar JDL)
com.bankernel.domain.enumeration/ → Enums (NÃO editar - usar JDL)
com.bankernel.repository/         → Spring Data JPA
com.bankernel.service/            → Lógica de negócio (EDITAR AQUI)
com.bankernel.service.dto/        → DTOs gerados via MapStruct
com.bankernel.service.mapper/     → Mappers gerados via MapStruct
com.bankernel.service.criteria/   → Filtros JPA Criteria
com.bankernel.service.integracao/ → Interfaces de integração (Strategy Pattern)
com.bankernel.web.rest/           → REST Controllers
```

## Regras Importantes

### Entidades e JDL
- Geradas pelo JHipster a partir de `bankernel.jdl`
- Para modificar: editar JDL e regenerar
- NUNCA editar manualmente `domain/` ou `domain/enumeration/`

### Valores Monetários
- SEMPRE `BigDecimal`, NUNCA `Double`/`Float`
- `RoundingMode.HALF_UP` em cálculos

### Nomenclatura
- Tudo em português brasileiro
- Entidades: PascalCase (`PessoaFisica`, `DepositoPix`)
- Campos: camelCase (`valorEnviado`, `numeroReferencia`)
- Tabelas: snake_case com prefixo do módulo (`pes_pessoa_fisica`, `dep_deposito`)
- Enums: prefixo `Enum` + PascalCase (`EnumStatusTransacao`)
- Campo `situacao` (não `status`) para estados de entidade

### Integrações
- Ficam em `service/integracao/`
- Strategy Pattern: interface + implementação por provedor
- NUNCA acoplar código de provedor específico no core
- DTOs de integração são records Java (imutáveis)

### Transações
- `Transacao` usa referência polimórfica (`tipoEntidadeOrigem` + `idEntidadeOrigem`)
- NÃO adicionar relações diretas OneToOne entre Transacao e novas entidades
- Toda operação financeira: `@Transactional`
- Toda operação: `numeroReferencia` único (idempotência)

### Contabilidade
- Toda movimentação gera lançamento contábil (débito + crédito)

## Módulos e Prefixos

| Módulo | Prefixo | Entidades |
|--------|---------|-----------|
| Pessoa | `pes_` | PessoaFisica, PessoaJuridica, Endereco, ColaboradorPJ, PermissaoColaborador |
| Carteira | `car_`, `moe_` | Carteira, MoedaCarteira, Moeda |
| Movimentação | `mov_` | Transacao, TipoOperacao, HistoricoOperacao |
| Depósito | `dep_` | Deposito, DepositoPix, DepositoBoleto |
| Saque | `saq_` | Saque, SaquePix, SaqueBoleto, ContaBancaria |
| Transferência | `trf_` | Transferencia |
| Cobrança | `cob_` | LinkCobranca, Cobranca, PlanoRecorrencia, AssinaturaRecorrencia |
| Contabilidade | `ctb_` | ContaContabil, LancamentoContabil |
| Configuração | `conf_` | Plano, ConfiguracaoSistema, BloqueioOperacao, Feriado |
| Gestão | `ges_` | Escritorio, Administrador |
| Notificação | `ntf_` | Notificacao |
| Integração | `itg_` | RegistroIntegracao |
| Referência | `ref_` | Pais, Profissao, TipoNegocio, BancoReferencia, Documento |

## Interfaces de Integração

### PIX (`service/integracao/pix/`)
- `ServicoPixIntegracao` - 8 métodos
- DTOs: RequisicaoPixEstatico, RequisicaoPixDinamico, RequisicaoPixDinamicoVencimento, RequisicaoEnvioPixChave, RequisicaoEnvioPixManual, RespostaPixQrCode, RespostaConsultaPix, RespostaEnvioPix, RespostaDevolucaoPix

### Boleto (`service/integracao/boleto/`)
- `ServicoBoletoIntegracao` - 5 métodos
- DTOs: RequisicaoBoleto (Sacado, Multa, Juros, Desconto, Mensagens, SacadorAvalista), RespostaBoleto, RespostaConsultaBoleto, RequisicaoPagamentoBoleto, RespostaPagamentoBoleto, RespostaConsultaPagamentoBoleto

### Mensageria (`service/integracao/mensageria/`)
- `ServicoMensageria`, `EventoDominio`, `ConsumidorEvento`

### Notificação (`service/integracao/notificacao/`)
- `ServicoNotificacao` - enviarEmail/enviarPush/enviarSms

## Fluxos Principais

### Depósito PIX
1. Criar `Deposito` (situacao: PENDENTE) + `DepositoPix` (situacao: EM_PROCESSO)
2. `ServicoPixIntegracao.criarPixDinamicoImediato()` → QR Code
3. Webhook → evento na mensageria → creditar carteira → lançamento contábil

### Saque PIX
1. Verificar saldo `Carteira` → `Saque` + `SaquePix`
2. Debitar saldo → `ServicoPixIntegracao.enviarPixPorChave()`
3. Atualizar situação → lançamento contábil

### Transferência P2P
1. Verificar saldo → `Transferencia`
2. Debitar origem, creditar destino → `Transacao` + `HistoricoOperacao` ambos lados
