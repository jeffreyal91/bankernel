# Implementando um Provedor Boleto

## Passo a Passo

### 1. Criar a Classe de Implementação

```java
package com.bankernel.service.integracao.boleto.impl;

import com.bankernel.service.integracao.boleto.*;
import com.bankernel.service.integracao.boleto.dto.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("meu-provedor-boleto")
public class MeuProvedorBoletoService implements ServicoBoletoIntegracao {

    @Override
    public RespostaBoleto gerarBoleto(RequisicaoBoleto requisicao) {
        // Campos obrigatórios do sacado:
        // - requisicao.sacado().nome()
        // - requisicao.sacado().documento() (CPF ou CNPJ)
        // - requisicao.sacado().tipoPessoa()
        // - requisicao.sacado().cep(), estado(), cidade(), logradouro()
        //
        // Campos financeiros:
        // - requisicao.valorNominal()
        // - requisicao.dataVencimento()
        // - requisicao.multa() (opcional)
        // - requisicao.juros() (opcional)
        // - requisicao.desconto() (opcional)
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaConsultaBoleto consultarBoleto(String idBoleto) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public boolean cancelarBoleto(String idBoleto) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaPagamentoBoleto pagarBoleto(RequisicaoPagamentoBoleto requisicao) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }

    @Override
    public RespostaConsultaPagamentoBoleto consultarPagamento(String idPagamento) {
        throw new UnsupportedOperationException("Implementar para seu provedor");
    }
}
```

### 2. Exemplo de Geração de Boleto

```java
var boleto = new RequisicaoBoleto(
    new BigDecimal("250.00"),                           // valorNominal
    LocalDate.of(2024, 4, 15),                          // dataVencimento
    new RequisicaoBoleto.Sacado(                        // sacado
        "João da Silva",
        "12345678901",
        "PESSOA_FISICA",
        "joao@email.com",
        "11999999999",
        "01310100", "SP", "São Paulo", "Bela Vista",
        "Av Paulista", "1000", "Sala 101"
    ),
    new RequisicaoBoleto.Multa("PERCENTUAL", new BigDecimal("2.00"), LocalDate.of(2024, 4, 16)),
    new RequisicaoBoleto.Juros("TAXA_MENSAL", new BigDecimal("1.00")),
    new RequisicaoBoleto.Desconto("FIXO_ATE_DATA", new BigDecimal("10.00"), LocalDate.of(2024, 4, 10)),
    new RequisicaoBoleto.Mensagens("Não receber após vencimento", "Sujeito a juros e multa", null, null),
    null,                                                // sacadorAvalista
    "DOC-2024-001",                                     // seuNumero
    "DM",                                                // especieDocumento
    true                                                 // aceite
);
```
