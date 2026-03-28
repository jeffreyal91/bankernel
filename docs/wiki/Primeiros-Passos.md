# Primeiros Passos

## 1. Pré-requisitos

- **Java 21+**: `java --version` deve retornar 21 ou superior
- **PostgreSQL 15+**: `psql --version`
- **Maven 3.9+**: (incluso via `./mvnw`)

## 2. Clonar e Configurar

```bash
git clone https://github.com/jeffreyal91/bankernel.git
cd bankernel
```

## 3. Criar Banco de Dados

```bash
createdb bankernel
```

Ou via psql:
```sql
CREATE DATABASE bankernel;
CREATE USER bankernel WITH PASSWORD 'bankernel';
GRANT ALL PRIVILEGES ON DATABASE bankernel TO bankernel;
```

## 4. Executar

```bash
./mvnw
```

O Liquibase aplica automaticamente todas as 65 migrações na primeira execução.

## 5. Acessar

| Recurso | URL |
|---------|-----|
| API REST | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| Health Check | http://localhost:8080/management/health |

## 6. Autenticar

### Obter Token JWT

```bash
curl -X POST http://localhost:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

Resposta:
```json
{
  "id_token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

### Usar Token

```bash
curl http://localhost:8080/api/carteiras \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## 7. Usuários Padrão

| Usuário | Senha | Role |
|---------|-------|------|
| admin | admin | ROLE_ADMIN + ROLE_USER |
| user | user | ROLE_USER |

**Importante**: Trocar senhas imediatamente em produção.

## 8. Configuração

Arquivo principal: `src/main/resources/config/application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bankernel
    username: bankernel
    password: bankernel
```

## 9. Próximos Passos

1. Explorar a API via Swagger UI
2. Criar entidades de referência (Moeda, Pais, Profissao)
3. Criar PessoaFisica vinculada a um User
4. Criar Carteira para o usuário
5. Implementar lógica de negócio nos Services
6. Implementar um provedor PIX/Boleto
