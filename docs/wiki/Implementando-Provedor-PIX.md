# Implementando um Provedor PIX

## Passo a Passo

### 1. Criar a Classe de Implementação

```java
package com.bankernel.service.integracao.pix.impl;

import com.bankernel.service.integracao.pix.*;
import com.bankernel.service.integracao.pix.dto.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@Profile("meu-provedor-pix")
public class MeuProvedorPixService implements ServicoPixIntegracao {

    @Override
    public RespostaPixQrCode criarPixEstatico(RequisicaoPixEstatico requisicao) {
        // 1. Autenticar com o provedor (OAuth2, mTLS, API Key)
        // 2. Montar payload conforme API do provedor
        // 3. Fazer chamada HTTP (RestTemplate, WebClient, etc.)
        // 4. Deserializar resposta
        // 5. Mapear para RespostaPixQrCode
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaPixQrCode criarPixDinamicoImediato(RequisicaoPixDinamico requisicao) {
        // Cobrança imediata - campos obrigatórios:
        // - requisicao.chavePix()
        // - requisicao.valor()
        // - requisicao.expiracaoSegundos()
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaPixQrCode criarPixDinamicoVencimento(RequisicaoPixDinamicoVencimento requisicao) {
        // Cobrança com vencimento - campos adicionais:
        // - requisicao.dataVencimento()
        // - requisicao.juros() (opcional)
        // - requisicao.multa() (opcional)
        // - requisicao.desconto() (opcional)
        // - requisicao.abatimento() (opcional)
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaConsultaPix consultarCobranca(String txId) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaEnvioPix enviarPixPorChave(RequisicaoEnvioPixChave requisicao) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaEnvioPix enviarPixManual(RequisicaoEnvioPixManual requisicao) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaDevolucaoPix solicitarDevolucao(String endToEndId, BigDecimal valor, String motivo) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public void configurarWebhook(String chave, String webhookUrl) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }
}
```

### 2. Ativar via Profile

```yaml
# application-meu-provedor-pix.yml
spring:
  profiles:
    active: dev,meu-provedor-pix

provedor-pix:
  url-base: https://api.meuprovedor.com.br
  client-id: meu-client-id
  client-secret: meu-client-secret
  certificado: /caminho/certificado.pfx
  senha-certificado: senha
```

### 3. Executar

```bash
./mvnw -Dspring-boot.run.profiles=dev,meu-provedor-pix
```

## DTOs de Referência

### Request: `RequisicaoPixDinamico`
```java
new RequisicaoPixDinamico(
    "chave@email.com",           // chavePix
    new BigDecimal("150.00"),    // valor
    3600,                         // expiracaoSegundos (1 hora)
    "12345678901",               // devedorCpf
    null,                         // devedorCnpj
    "João da Silva",             // devedorNome
    "Pagamento pedido #123",     // descricao
    List.of(                     // informacoesAdicionais
        new InfoAdicional("Pedido", "123"),
        new InfoAdicional("Parcela", "1/3")
    )
);
```

### Response: `RespostaPixQrCode`
```java
new RespostaPixQrCode(
    "txid123abc",                               // txId
    "qrcodepix.example.com/api/v2/txid123abc", // location
    "00020126580014br.gov.bcb.pix...",          // pixCopiaECola
    "iVBORw0KGgoAAAANSUhEUg...",               // qrCodeBase64
    Instant.now(),                               // criadoEm
    Instant.now().plusSeconds(3600),             // expiraEm
    "ATIVA"                                      // status
);
```

## Tratamento de Erros

Use `IntegracaoPixException` para todos os erros:

```java
throw new IntegracaoPixException(
    "Falha ao criar cobrança PIX",  // mensagem
    "PIX_INVALID_KEY",               // codigoErro do provedor
    400                               // statusHttp
);
```
