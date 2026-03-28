# Módulos e Entidades

## Visão Geral

38 entidades organizadas em 13 módulos, com 52 enumerações.

## Diagrama de Relacionamentos

```
Usuario (JHipster)
├── 1:1 → PessoaFisica ──→ Pais, Profissao, Plano, Escritorio, MoedaCarteira
├── 1:1 → PessoaJuridica ──→ Pais, TipoNegocio, Plano, Escritorio, MoedaCarteira, Documento
├── 1:1 → ColaboradorPJ ──→ PessoaJuridica
│                └──→ PermissaoColaborador ──→ Carteira
├── 1:1 → Administrador ──→ Escritorio
├── N:1 ← Endereco
├── N:1 ← Carteira ──→ MoedaCarteira ──→ Moeda
├── N:1 ← Deposito ──→ Transacao, Carteira, MoedaCarteira, ContaBancaria
│          ├── 1:1 ← DepositoPix
│          └── 1:1 ← DepositoBoleto
├── N:1 ← Saque ──→ Transacao (2x), Carteira, MoedaCarteira, ContaBancaria, Escritorio
│          ├── 1:1 ← SaquePix
│          └── 1:1 ← SaqueBoleto
├── N:1 ← Transferencia ──→ Transacao, Carteira (2x), MoedaCarteira
├── N:1 ← Cobranca ──→ Transacao, Carteira (2x), MoedaCarteira, LinkCobranca
├── N:1 ← LinkCobranca ──→ MoedaCarteira
├── N:1 ← PlanoRecorrencia ──→ LinkCobranca
├── N:1 ← AssinaturaRecorrencia ──→ PlanoRecorrencia, LinkCobranca
├── N:1 ← ContaBancaria ──→ Pais, Moeda
├── N:1 ← Notificacao
└── N:1 ← HistoricoOperacao ──→ Transacao, Carteira

Transacao ──→ Carteira (origem/destino), MoedaCarteira (origem/destino)
TipoOperacao ──→ ContaContabil (crédito/débito), MoedaCarteira
ContaContabil ──→ MoedaCarteira
LancamentoContabil ──→ Transacao, ContaContabil
```

## Módulo Pessoa (`pes_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| PessoaFisica | `pes_pessoa_fisica` | cpf (único), nomeCompleto, dataNascimento, nomeMae, telefone, nivelRisco, situacao |
| PessoaJuridica | `pes_pessoa_juridica` | cnpj (único), razaoSocial, nomeFantasia, capitalSocial, atividadePrincipal, nivelRisco |
| Endereco | `pes_endereco` | cep, logradouro, numero, bairro, cidade, estado, tipoEndereco, principal |
| ColaboradorPJ | `pes_colaborador_pj` | ativo, departamento |
| PermissaoColaborador | `pes_permissao_colaborador` | tipoPermissao (VISUALIZAR, OPERAR, ADMINISTRAR) |

## Módulo Carteira (`car_`, `moe_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| Moeda | `moe_moeda` | codigoMoeda (BRL, USD, EUR, GBP), nome, ativa |
| MoedaCarteira | `car_moeda_carteira` | codigo (único), fatorConversao, casasDecimais, simbolo, principal |
| Carteira | `car_carteira` | saldo (BigDecimal), limiteNegativo, valorCongelado, numeroConta (único), ativa |

## Módulo Movimentação (`mov_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| TipoOperacao | `mov_tipo_operacao` | codigo (enum), sinalCredito, sinalDebito, contaCredito, contaDebito |
| Transacao | `mov_transacao` | valorEnviado, valorRecebido, tipoTransacao, tipoPagamento, situacao, tipoEntidadeOrigem, idEntidadeOrigem |
| HistoricoOperacao | `mov_historico_operacao` | valor, saldoApos, tipoSimbolo (ENTRADA/SAIDA), tipoHistorico |

## Módulo Depósito (`dep_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| Deposito | `dep_deposito` | valor, tipoDeposito (PIX/BOLETO), situacaoDeposito, numeroReferencia, contabilizado |
| DepositoPix | `dep_deposito_pix` | tipo (QR_ESTATICO/DINAMICO/VENCIMENTO), codigoQr, identificadorTransacao, identificadorPontaAPonta, pagadorNome/Cpf/Cnpj |
| DepositoBoleto | `dep_deposito_boleto` | codigoBarras, linhaDigitavel, dataVencimento, pagadorNome/Cpf/Cnpj |

## Módulo Saque (`saq_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| Saque | `saq_saque` | valorSaque, tipoSaque (PIX_CHAVE/MANUAL/QR/BOLETO/TED), situacaoSaque, numeroReferencia |
| SaquePix | `saq_saque_pix` | tipo (CHAVE/MANUAL/QR_CODE), identificadorPagamento, identificadorPontaAPonta |
| SaqueBoleto | `saq_saque_boleto` | tipo, codigoBarras |
| ContaBancaria | `saq_conta_bancaria` | nomeTitular, numeroConta, agencia, nomeBanco, codigoBanco, ispb, codigoSwift, tipoConta |

## Módulo Transferência (`trf_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| Transferencia | `trf_transferencia` | valor, chaveInterna, tipoChave (EMAIL/CPF/TELEFONE/CONTA), situacao, numeroReferencia |

## Módulo Cobrança (`cob_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| LinkCobranca | `cob_link_cobranca` | nome, chaveAcesso, urlRetorno, tipo (UNICO/RECORRENTE/DOACAO), situacao, desconto |
| Cobranca | `cob_cobranca` | valor, situacao, tipo (PIX/BOLETO), chaveCobranca, retornoNotificado |
| PlanoRecorrencia | `cob_plano_recorrencia` | nome, valor, intervalo, parcelas, tentativas, metodoPagamento |
| AssinaturaRecorrencia | `cob_assinatura_recorrencia` | devedorNome/Documento/Email, tipo, tipoPagamento, situacao |

## Módulo Contabilidade (`ctb_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| ContaContabil | `ctb_conta_contabil` | codigo (único), saldo (BigDecimal), tipoContaContabil (ATIVO/PASSIVO/RECEITA/DESPESA), categoriaContaContabil |
| LancamentoContabil | `ctb_lancamento_contabil` | valor, tipoLancamento, sinalLancamento (DEBITO/CREDITO) |

## Módulo Configuração (`conf_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| Plano | `conf_plano` | nome, ativo, padrao |
| ConfiguracaoSistema | `conf_configuracao_sistema` | chave (única), valor, tipo (STRING/INTEGER/BOOLEAN/BIG_DECIMAL), modulo |
| BloqueioOperacao | `conf_bloqueio_operacao` | tipoOperacao, diaSemana, horaInicio, horaFim, tipoExecucao |
| Feriado | `conf_feriado` | data, descricao |

## Módulo Gestão (`ges_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| Escritorio | `ges_escritorio` | nome, ativo |
| Administrador | `ges_administrador` | ativo |

## Módulo Notificação (`ntf_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| Notificacao | `ntf_notificacao` | titulo, mensagem, tipo (EMAIL/PUSH/SMS/INTERNA), situacao, canal, lida |

## Módulo Integração (`itg_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| RegistroIntegracao | `itg_registro_integracao` | fornecedor, tipoIntegracao, operacao, corpoRequisicao, corpoResposta, codigoHttp, sucesso, duracaoMilissegundos |

## Referência (`ref_`)

| Entidade | Tabela | Campos Chave |
|----------|--------|-------------|
| Pais | `ref_pais` | codigo (ISO 3166), nome |
| Profissao | `ref_profissao` | nome |
| TipoNegocio | `ref_tipo_negocio` | nome |
| BancoReferencia | `ref_banco` | codigo, nome, ispb |
| Documento | `ref_documento` | nome, tipoArquivo, endereco, tamanho |
