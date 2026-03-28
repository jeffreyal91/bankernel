# Integrações Bancárias Planejadas

Cada integração bancária implementa as interfaces `ServicoPixIntegracao` e `ServicoBoletoIntegracao` já definidas no core. Nenhuma modificação no kernel é necessária.

## Estrutura por Banco

Cada banco será um módulo independente:

```
bankernel-integracao-bb/          # Banco do Brasil
bankernel-integracao-itau/        # Itaú Unibanco
bankernel-integracao-bradesco/    # Bradesco
bankernel-integracao-caixa/       # Caixa Econômica Federal
bankernel-integracao-santander/   # Santander
```

Ativação via Spring Profile:
```yaml
spring:
  profiles:
    active: dev,banco-do-brasil
```

---

## 1. Banco do Brasil

**API**: [Developers BB](https://developers.bb.com.br/)

### PIX
| Funcionalidade | API BB | Método BanKernel |
|---------------|--------|-----------------|
| Criar cobrança imediata | `POST /pix/cob` | `criarPixDinamicoImediato()` |
| Criar cobrança com vencimento | `POST /pix/cobv` | `criarPixDinamicoVencimento()` |
| Consultar cobrança | `GET /pix/cob/{txid}` | `consultarCobranca()` |
| Enviar PIX | `POST /pix/pagamentos` | `enviarPixPorChave()` |
| Devolução | `PUT /pix/devolucao/{e2eid}` | `solicitarDevolucao()` |
| Webhook | `PUT /pix/webhook/{chave}` | `configurarWebhook()` |

### Boleto
| Funcionalidade | API BB | Método BanKernel |
|---------------|--------|-----------------|
| Registrar boleto | `POST /boletos` | `gerarBoleto()` |
| Consultar boleto | `GET /boletos/{nossoNumero}` | `consultarBoleto()` |
| Baixar boleto | `POST /boletos/{nossoNumero}/baixar` | `cancelarBoleto()` |

### Autenticação
- OAuth2 Client Credentials
- Certificado digital (mTLS) para produção
- Ambiente sandbox disponível

### Particularidades
- PIX: suporta QR Code estático e dinâmico
- Boleto: suporta CNAB 240 e 400 para retorno
- Limite de 10 requisições/segundo por credencial
- Webhook com verificação de assinatura

---

## 2. Itaú Unibanco

**API**: [Itaú Developers](https://developer.itau.com.br/)

### PIX
| Funcionalidade | API Itaú | Método BanKernel |
|---------------|----------|-----------------|
| Criar cobrança | `POST /cob` | `criarPixDinamicoImediato()` |
| Cobrança com vencimento | `POST /cobv` | `criarPixDinamicoVencimento()` |
| Consultar | `GET /cob/{txid}` | `consultarCobranca()` |
| Pagamento por chave | `POST /pagamentos/chave` | `enviarPixPorChave()` |
| Pagamento manual | `POST /pagamentos/dados-bancarios` | `enviarPixManual()` |
| Devolução | `PUT /pix/{e2eid}/devolucao` | `solicitarDevolucao()` |

### Boleto
| Funcionalidade | API Itaú | Método BanKernel |
|---------------|----------|-----------------|
| Emitir boleto | `POST /boletos` | `gerarBoleto()` |
| Consultar boleto | `GET /boletos/{idBoleto}` | `consultarBoleto()` |
| Baixar boleto | `PATCH /boletos/{idBoleto}` | `cancelarBoleto()` |
| Pagar boleto | `POST /pagamentos/boleto` | `pagarBoleto()` |

### Autenticação
- OAuth2 com client_id e client_secret
- mTLS obrigatório em produção
- Certificado ICP-Brasil (e-CNPJ ou e-CPF)

### Particularidades
- PIX: suporta payload com informações adicionais
- Boleto: suporta multa progressiva e juros compostos
- Rate limit: 100 req/min
- Suporte a BolePIX (boleto + QR PIX no mesmo documento)

---

## 3. Bradesco

**API**: [Bradesco Developers](https://developers.bradesco.com.br/)

### PIX
| Funcionalidade | API Bradesco | Método BanKernel |
|---------------|-------------|-----------------|
| Criar cobrança | `POST /v2/cob` | `criarPixDinamicoImediato()` |
| Cobrança com vencimento | `POST /v2/cobv` | `criarPixDinamicoVencimento()` |
| Consultar | `GET /v2/cob/{txid}` | `consultarCobranca()` |
| Enviar PIX | `POST /v1/spi/pagamento` | `enviarPixPorChave()` |
| Devolução | `PUT /v2/pix/{e2eid}/devolucao` | `solicitarDevolucao()` |

### Boleto
| Funcionalidade | API Bradesco | Método BanKernel |
|---------------|-------------|-----------------|
| Registrar boleto | `POST /v1/boleto/registrar` | `gerarBoleto()` |
| Consultar boleto | `GET /v1/boleto/{nossoNumero}` | `consultarBoleto()` |
| Cancelar boleto | `POST /v1/boleto/baixar` | `cancelarBoleto()` |

### Autenticação
- OAuth2 + certificado digital
- JWT assinado com certificado do cliente
- Ambiente homologação disponível

### Particularidades
- PIX: webhook com HMAC-SHA256 para verificação
- Boleto: carteiras 09 (sem registro) e 26 (com registro)
- Suporte a boleto híbrido (PIX + boleto)
- API versão 2 para PIX (v2)

---

## 4. Caixa Econômica Federal

**API**: [Caixa Developers](https://desenvolvedores.caixa.gov.br/)

### PIX
| Funcionalidade | API Caixa | Método BanKernel |
|---------------|----------|-----------------|
| Criar cobrança | `POST /pix/cob` | `criarPixDinamicoImediato()` |
| Cobrança com vencimento | `POST /pix/cobv` | `criarPixDinamicoVencimento()` |
| Consultar | `GET /pix/cob/{txid}` | `consultarCobranca()` |
| Enviar PIX | `POST /pix/pagamento` | `enviarPixPorChave()` |
| Devolução | `PUT /pix/{e2eid}/devolucao` | `solicitarDevolucao()` |

### Boleto
| Funcionalidade | API Caixa | Método BanKernel |
|---------------|----------|-----------------|
| Registrar boleto (SIGCB) | `POST /cobranca-bancaria/boletos` | `gerarBoleto()` |
| Consultar boleto | `GET /cobranca-bancaria/boletos/{nossoNumero}` | `consultarBoleto()` |
| Baixar boleto | `PUT /cobranca-bancaria/boletos/{nossoNumero}/baixa` | `cancelarBoleto()` |

### Autenticação
- OAuth2 Client Credentials
- Certificado digital ICP-Brasil
- Chave de acesso por convênio

### Particularidades
- Boleto: sistema SIGCB com numeração própria
- PIX: integração via BACEN (SPI direto)
- Suporte a FGTS e programas governamentais
- Ambiente de homologação com dados simulados

---

## 5. Santander

**API**: [Santander Developer](https://developer.santander.com.br/)

### PIX
| Funcionalidade | API Santander | Método BanKernel |
|---------------|--------------|-----------------|
| Criar cobrança | `POST /pix/cob` | `criarPixDinamicoImediato()` |
| Cobrança com vencimento | `POST /pix/cobv` | `criarPixDinamicoVencimento()` |
| Consultar | `GET /pix/cob/{txid}` | `consultarCobranca()` |
| Enviar PIX | `POST /pix/pagamentos` | `enviarPixPorChave()` |
| Devolução | `PUT /pix/{e2eid}/devolucao` | `solicitarDevolucao()` |

### Boleto
| Funcionalidade | API Santander | Método BanKernel |
|---------------|--------------|-----------------|
| Registrar boleto | `POST /collection/boletos` | `gerarBoleto()` |
| Consultar boleto | `GET /collection/boletos/{nossoNumero}` | `consultarBoleto()` |
| Baixar boleto | `PATCH /collection/boletos/{nossoNumero}` | `cancelarBoleto()` |
| Pagar boleto | `POST /payments/boletos` | `pagarBoleto()` |

### Autenticação
- OAuth2 com workspace e client_id
- Certificado mTLS para chamadas
- X-Application-Key para identificação

### Particularidades
- PIX: suporta PIX Saque e PIX Troco
- Boleto: registro online com resposta síncrona
- Rate limit: 50 req/min por credencial
- API de conciliação disponível

---

## Matriz de Funcionalidades por Banco

| Funcionalidade | BB | Itaú | Bradesco | Caixa | Santander |
|---------------|:--:|:----:|:--------:|:-----:|:---------:|
| PIX Estático | - | - | - | - | - |
| PIX Dinâmico Imediato | - | - | - | - | - |
| PIX Dinâmico Vencimento | - | - | - | - | - |
| PIX Envio por Chave | - | - | - | - | - |
| PIX Envio Manual | - | - | - | - | - |
| PIX Devolução | - | - | - | - | - |
| PIX Webhook | - | - | - | - | - |
| Boleto Emissão | - | - | - | - | - |
| Boleto Consulta | - | - | - | - | - |
| Boleto Cancelamento | - | - | - | - | - |
| Boleto Pagamento | - | - | - | - | - |
| BolePIX | - | - | - | - | - |

**Legenda:** `-` = planejado (nenhuma implementação incluída no core)

## Como Contribuir com uma Integração

1. Crie um novo módulo Maven ou pacote Spring
2. Implemente `ServicoPixIntegracao` e/ou `ServicoBoletoIntegracao`
3. Use `@Service` + `@Profile("nome-do-banco")`
4. Inclua testes com mocks da API do banco
5. Documente as particularidades de autenticação
6. Não adicione dependências do banco ao core do BanKernel
