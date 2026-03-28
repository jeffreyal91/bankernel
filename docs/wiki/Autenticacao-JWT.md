# Autenticação JWT

## Visão Geral

O BanKernel usa JWT (JSON Web Token) Bearer Token para autenticação stateless.

## Fluxo de Autenticação

```
1. Cliente envia POST /api/authenticate com username + password
2. Servidor valida credenciais (BCrypt)
3. Servidor gera JWT assinado com HMAC-SHA512
4. Cliente inclui token em todas as requisições: Authorization: Bearer <token>
5. Servidor valida assinatura e extrai authorities do token
```

## Obter Token

```bash
curl -X POST http://localhost:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin",
    "rememberMe": false
  }'
```

Resposta:
```json
{
  "id_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcxMTEyMDAwMCwiYXV0aCI6IlJPTEVfQURNSU4sUk9MRV9VU0VSIiwiaWF0IjoxNzExMDMzNjAwfQ.signature"
}
```

## Usar Token

```bash
curl http://localhost:8080/api/carteiras \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## Configuração do Token

`src/main/resources/config/application.yml`:

```yaml
jhipster:
  security:
    authentication:
      jwt:
        base64-secret: <chave-secreta-base64>
        token-validity-in-seconds: 86400        # 24 horas
        token-validity-in-seconds-for-remember-me: 2592000  # 30 dias
```

## Roles

| Role | Acesso |
|------|--------|
| `ROLE_USER` | Todos os endpoints `/api/**` |
| `ROLE_ADMIN` | `/api/**` + `/api/admin/**` + `/management/**` + Swagger UI |

## Endpoints Públicos (sem autenticação)

- `POST /api/authenticate` - Login
- `POST /api/register` - Registro de novo usuário
- `POST /api/activate` - Ativação de conta
- `POST /api/account/reset-password/init` - Solicitar reset de senha
- `POST /api/account/reset-password/finish` - Completar reset de senha
- `GET /management/health` - Health check

## Registrar Novo Usuário

```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "login": "novousuario",
    "email": "novo@email.com",
    "password": "senhasegura123",
    "langKey": "pt-br"
  }'
```

## Estrutura do Token JWT

Payload decodificado:
```json
{
  "sub": "admin",
  "auth": "ROLE_ADMIN,ROLE_USER",
  "iat": 1711033600,
  "exp": 1711120000
}
```

- `sub`: username
- `auth`: authorities (roles) separadas por vírgula
- `iat`: data de emissão (Unix timestamp)
- `exp`: data de expiração (Unix timestamp)
