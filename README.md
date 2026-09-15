# CampusGigs — API REST

> Plataforma de freelas entre alunos de universidade — um aluno se cadastra e publica um serviço, outro aluno, autenticado, contrata esse serviço.

Projeto Diamante — Java Advanced (2º Semestre)

---

## 📋 Sobre o projeto

A CampusGigs sustenta o fluxo completo de:

1. Cadastro e autenticação de usuários (senha protegida, token JWT)
2. Publicação de serviços (freelas) por alunos autenticados
3. Contratação de serviços por outros alunos
4. Controle de acesso por papel (`ADMIN` / `USER`)

## 🛠 Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 25 |
| Framework | Spring Boot 4.1 |
| Segurança | Spring Security + JWT (jjwt) |
| Persistência | Spring Data JPA |
| Banco de dados | Oracle Database Free (via Docker) |
| Migrations | Flyway |
| Integração externa | Spring `HttpExchange` (cliente declarativo, ViaCEP) |
| Containerização | Docker / Docker Compose |

## 🗂 Domínio

User (usuário) Gig (serviço) Contract (contratação)
├─ name ├─ provider (User) ├─ gig (Gig)
├─ email (único) ├─ title ├─ hirer (User)
├─ passwordHash ├─ description └─ status
├─ role: ADMIN | USER ├─ category REQUESTED
├─ zipCode / city / state ├─ price ACCEPTED
└─ status COMPLETED
ACTIVE CANCELLED
PAUSED
CLOSED


## ✅ Checkpoints

| # | Entrega | Status |
|---|---|---|
| CP1 | Ambiente sobe via Docker; primeira migration | ✅ |
| CP2 | Cadastro e autenticação (senha protegida) | ✅ |
| CP3 | Emissão e validação de JWT | ✅ |
| CP4 | Regras de autorização por papel | ✅ |
| CP5 | Integração HttpExchange com ViaCEP | ✅ |
Commits extras para ajustes.
---

## 🚀 Como rodar

### Pré-requisitos
- Docker Desktop instalado e aberto

### Subir o ambiente completo

```bash
docker compose up --build
```

Isso sobe automaticamente:
- **Oracle Database Free** — porta `1521`
- **API CampusGigs** — porta `8080`

O Flyway aplica todas as migrations sozinho na inicialização (schema `users`, `gigs`, `contracts`, e um usuário `ADMIN` de bootstrap).

### Encerrar

```bash
docker compose down       # mantém os dados
docker compose down -v    # apaga o volume, reseta o banco do zero
```

### Variáveis de ambiente (opcional — já vêm com valor padrão)

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_URL` | Oracle FIAP | URL de conexão JDBC |
| `DB_USER` / `DB_PASSWORD` | — | Credenciais do banco |
| `JWT_SECRET` | chave de dev | Segredo usado para assinar o token |
| `JWT_EXPIRATION_MINUTES` | `60` | Validade do token |
| `CEP_API_URL` | `https://viacep.com.br/ws` | Base URL do serviço de CEP |

---

## 🔑 Usuário ADMIN de bootstrap

Criado automaticamente pela migration `V4__seed_admin_user.sql`, para permitir testar operações restritas a papel `ADMIN` desde o primeiro boot:

email: admin@campusgigs.com
senha: admin1234


A partir dele, outros usuários podem ser promovidos via `PATCH /admin/users/{id}/promote`.

---

## 📡 Endpoints

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| `POST` | `/auth/register` | — | Cadastra um usuário |
| `POST` | `/auth/login` | — | Autentica e retorna o token JWT |
| `GET` | `/users/me` | ✅ | Dados do usuário logado |
| `PATCH` | `/users/me/zip-code` | ✅ | Atualiza CEP (busca cidade/UF via ViaCEP) |
| `POST` | `/gigs` | ✅ | Publica um serviço |
| `GET` | `/gigs` | — | Lista serviços ativos |
| `PATCH` | `/gigs/{id}/close` | ✅ (dono ou ADMIN) | Encerra um serviço |
| `POST` | `/contracts` | ✅ | Contrata um serviço |
| `PATCH` | `/admin/users/{id}/promote` | ✅ (somente ADMIN) | Promove um usuário a ADMIN |

## 💡 Exemplo de chamada autenticada

```bash
# 1. Cadastro
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Ana Silva","email":"ana@fiap.com.br","password":"senha1234","zipCode":"01310100"}'

# 2. Login → recebe o token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@fiap.com.br","password":"senha1234"}'

# Resposta:
# { "token": "eyJhbGciOi...", "tokenType": "Bearer", "expiresInMinutes": 60 }

# 3. Chamada autenticada, usando o token do passo anterior
curl -X POST http://localhost:8080/gigs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOi..." \
  -d '{"title":"Aula de Java","description":"Reforço para provas","category":"Educação","price":50}'
```

## ⚠️ Formato de erro (centralizado)

Toda violação de validação, autenticação, autorização ou regra de negócio responde nesse formato — nunca expõe stack trace:

```json
{
  "timestamp": "2026-09-14T20:20:41.662855",
  "status": 403,
  "error": "Forbidden",
  "message": "Você só pode encerrar os próprios serviços",
  "details": null
}
```

## 🧪 Evidência de teste — acesso negado por papel

Fluxo testado manualmente (prints em `/docs/evidencias` — *ajuste esse caminho pra onde você salvar os seus*):

1. Usuário comum tenta encerrar serviço de outro usuário → `403 Forbidden`
2. Mesmo serviço, mesma operação, executada por um `ADMIN` → `204 No Content` (sucesso)

---

## 📁 Estrutura do projeto

src/main/java/com/diamante/campusgigs/
├── client/ # HttpExchange (integração ViaCEP)
├── controller/ # Endpoints REST
├── entity/ # Entidades JPA + DTOs + enums
├── exception/ # Exceções customizadas + handler centralizado
├── repository/ # Spring Data JPA
├── security/ # JWT, Spring Security, autorização
└── service/ # Regras de negócio

INTEGRANTES:

Vitoria Valentina Maglio RM 563509
Marina Magalhaes RM 561786
