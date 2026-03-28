# Segurança e Validação de Identidade

## Visão Geral

Módulos planejados para KYC (Know Your Customer), validação de identidade, prevenção a fraudes e consulta de dados de pessoas físicas e jurídicas.

Assim como PIX e Boleto, todas as integrações de identidade seguirão o **Strategy Pattern** — interfaces no core, implementações por provedor.

---

## Interfaces Planejadas

### 1. `ServicoValidacaoDocumento`

Validação de CPF e CNPJ na Receita Federal.

```java
public interface ServicoValidacaoDocumento {

    /**
     * Consulta situação cadastral de CPF na Receita Federal.
     * @return dados: nome, data nascimento, situação (regular, suspensa, cancelada, etc.)
     */
    RespostaConsultaCpf consultarCpf(String cpf);

    /**
     * Consulta situação cadastral de CNPJ na Receita Federal.
     * @return dados: razão social, nome fantasia, CNAE, natureza jurídica,
     *         capital social, quadro societário, situação cadastral, endereço
     */
    RespostaConsultaCnpj consultarCnpj(String cnpj);

    /**
     * Valida se CPF/CNPJ é válido (dígito verificador + situação ativa).
     */
    boolean validarDocumento(String documento);
}
```

### 2. `ServicoReconhecimentoIdentidade`

OCR de documentos e reconhecimento facial.

```java
public interface ServicoReconhecimentoIdentidade {

    /**
     * Extrai dados de documento de identidade (RG, CNH, passaporte).
     * @param imagemFrente imagem da frente do documento (Base64 ou URL)
     * @param imagemVerso imagem do verso (opcional, depende do documento)
     * @return dados extraídos: nome, CPF, data nascimento, número documento, validade
     */
    RespostaOcrDocumento extrairDadosDocumento(String imagemFrente, String imagemVerso);

    /**
     * Verifica se a pessoa é real (liveness detection).
     * Detecta fotos de fotos, vídeos, máscaras.
     * @param imagemRosto selfie ou frame de vídeo
     * @return resultado: vivo/não-vivo, score de confiança
     */
    RespostaLiveness verificarVivacidade(String imagemRosto);

    /**
     * Compara rosto da selfie com foto do documento.
     * @return resultado: match/não-match, score de similaridade (0-100)
     */
    RespostaFaceMatch compararRostos(String imagemSelfie, String imagemDocumento);
}
```

### 3. `ServicoConsultaDados`

Consulta de dados em bureaus de crédito e bases públicas.

```java
public interface ServicoConsultaDados {

    /**
     * Consulta score de crédito de pessoa física.
     * @return score (0-1000), classificação de risco, renda estimada
     */
    RespostaScoreCredito consultarScorePF(String cpf);

    /**
     * Consulta score de crédito de pessoa jurídica.
     * @return score, classificação, faturamento estimado, tempo de fundação
     */
    RespostaScoreCredito consultarScorePJ(String cnpj);

    /**
     * Consulta dívidas e pendências financeiras.
     * @return lista de pendências: credor, valor, data, situação
     */
    RespostaConsultaPendencias consultarPendencias(String documento);

    /**
     * Consulta dados cadastrais completos (nome, endereço, telefone, email).
     * @return dados enriquecidos da pessoa
     */
    RespostaDadosCadastrais consultarDadosCadastrais(String documento);

    /**
     * Consulta se pessoa é PEP (Pessoa Exposta Politicamente).
     * @return se é PEP, cargo, órgão, período
     */
    RespostaConsultaPep consultarPep(String cpf);

    /**
     * Consulta listas restritivas (sanções internacionais).
     * @return se consta em listas OFAC, UE, ONU, BACEN
     */
    RespostaListasRestritivas consultarListasRestritivas(String nome, String documento);
}
```

### 4. `ServicoAnaliseRisco`

Análise de risco automatizada baseada em regras.

```java
public interface ServicoAnaliseRisco {

    /**
     * Calcula nível de risco de um cliente PF.
     * Considera: score, pendências, PEP, sanções, comportamento transacional.
     * @return nível (BAIXO, MEDIO, ALTO, CRITICO) com justificativa
     */
    RespostaAnaliseRisco analisarRiscoPF(String cpf, DadosComportamentais dados);

    /**
     * Calcula nível de risco de um cliente PJ.
     * Considera: score, quadro societário, tempo de fundação, processos.
     * @return nível com justificativa
     */
    RespostaAnaliseRisco analisarRiscoPJ(String cnpj, DadosComportamentais dados);

    /**
     * Analisa se uma transação específica é suspeita.
     * Considera: valor, horário, frequência, destino, padrão do usuário.
     * @return score de risco (0-100), recomendação (aprovar/revisar/bloquear)
     */
    RespostaAnaliseTransacao analisarTransacao(DadosTransacao transacao);
}
```

---

## Provedores Planejados

### Validação de Documentos
| Provedor | Serviço | Cobertura |
|----------|---------|-----------|
| Serpro | Consulta CPF/CNPJ via API oficial | CPF, CNPJ direto da Receita Federal |
| ReceitaWS | Consulta CNPJ gratuita (com limites) | CNPJ, quadro societário |

### Reconhecimento de Identidade (KYC)
| Provedor | Serviço | Capacidades |
|----------|---------|-------------|
| CAF (Combate à Fraude) | KYC completo brasileiro | OCR, liveness, face match, documentoscopia |
| Unico Check | Biometria facial | Liveness, face match, anti-fraude |
| iProov | Liveness detection | Verificação de vivacidade avançada |
| AWS Rekognition | Face match | Comparação facial via AWS |

### Consulta de Dados e Bureaus
| Provedor | Serviço | Dados |
|----------|---------|-------|
| Serasa Experian | Score de crédito | Score PF/PJ, pendências, renda estimada |
| SPC Brasil | Consulta de débitos | Pendências, protestos, cheques |
| Boa Vista SCPC | Score e dados | Score, histórico de pagamentos |
| BigDataCorp | Dados enriquecidos | Dados cadastrais, redes sociais, comportamento |
| Quod | Score de crédito | Cadastro Positivo, score |

### PEP e Sanções
| Provedor | Serviço | Cobertura |
|----------|---------|-----------|
| BACEN | Lista de PEPs | Pessoas expostas politicamente (Brasil) |
| COAF | Comunicações | Operações suspeitas |
| OFAC (US Treasury) | Sanções | Lista SDN internacional |
| Dow Jones | Risk & Compliance | PEP, sanções, mídia adversa global |

---

## Fluxo de Onboarding Planejado

```
1. Usuário informa CPF/CNPJ
2. ServicoValidacaoDocumento.consultarCpf() → verifica situação
3. ServicoConsultaDados.consultarDadosCadastrais() → enriquece dados
4. Usuário envia foto do documento + selfie
5. ServicoReconhecimentoIdentidade.extrairDadosDocumento() → OCR
6. ServicoReconhecimentoIdentidade.verificarVivacidade() → liveness
7. ServicoReconhecimentoIdentidade.compararRostos() → face match
8. ServicoConsultaDados.consultarPep() → verifica PEP
9. ServicoConsultaDados.consultarListasRestritivas() → verifica sanções
10. ServicoAnaliseRisco.analisarRiscoPF() → calcula nível de risco
11. Resultado: APROVADO / PENDENTE_REVISAO / REJEITADO
```

## Entidades de Suporte (Futuras)

- `ValidacaoIdentidade` - registro de cada validação realizada
- `DocumentoIdentidade` - imagens e dados extraídos
- `ConsultaBureau` - log de consultas a bureaus
- `AnaliseRiscoHistorico` - histórico de análises de risco
- `ListaRestritiva` - cache local de listas restritivas
