# Guia de Contribuição - BanKernel

## Como Contribuir

### 1. Entidades e Banco de Dados

Entidades são geradas pelo JHipster a partir de `bankernel.jdl`.

**Para adicionar ou modificar entidades:**
1. Edite `bankernel.jdl`
2. Execute `jhipster jdl bankernel.jdl --force`
3. Nunca edite manualmente `domain/` ou `domain/enumeration/`

### 2. Lógica de Negócio

Implemente nos `*Service.java` em `src/main/java/com/bankernel/service/`.

### 3. Integrações

Para implementar um novo provedor:

1. Crie classe que implementa a interface:
   - `ServicoPixIntegracao` para PIX
   - `ServicoBoletoIntegracao` para Boleto
   - `ServicoMensageria` para mensageria
   - `ServicoNotificacao` para notificações

2. Use `@Service` e `@Profile("nome-do-provedor")`

3. Coloque em `service/integracao/{modulo}/impl/`

4. Nunca modifique as interfaces

### 4. Convenções

- **Idioma:** Todo código em português
- **Valores monetários:** Sempre `BigDecimal`
- **Campos de situação:** Usar `situacao`, não `status`
- **Commits:** Em português
- **Testes:** Todo service novo deve ter testes

### 5. Executar Localmente

```bash
createdb bankernel
./mvnw compile   # Compilar
./mvnw test      # Testes
./mvnw           # Executar
```

### 6. Checklist antes do PR

- [ ] Compila sem erros (`./mvnw compile`)
- [ ] Testes passam (`./mvnw test`)
- [ ] Valores monetários usam `BigDecimal`
- [ ] Nomes em português
- [ ] Sem código de provedor específico no core
- [ ] Operações financeiras são `@Transactional`
