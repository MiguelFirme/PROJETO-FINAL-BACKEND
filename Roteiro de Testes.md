# Roteiro de Testes - API de Finanças e Investimentos

**Autor:** Manus AI

---

## 1. Introdução

Este documento detalha o roteiro de testes para a API de Finanças e Investimentos. O objetivo é validar as funcionalidades da aplicação, garantir a integridade dos dados e a segurança da API. Os testes serão realizados utilizando um cliente de API como o Postman ou `curl`.

## 2. Pré-requisitos

- A aplicação deve estar em execução.
- O banco de dados MySQL deve estar acessível.
- As credenciais de autenticação básica (`user` e `123456789`) devem ser usadas em todas as requisições.

## 3. Casos de Teste

### 3.1. API de Usuários (`/api/usuarios`)

| Cenário | Método | Endpoint | Corpo da Requisição (JSON) | Resultado Esperado |
|---|---|---|---|---|
| **Criar um novo usuário** | `POST` | `/api/usuarios` | `{"nome": "Teste User", "email": "teste@email.com", "senha": "senha123"}` | **Status 201 Created** com o novo usuário no corpo da resposta. |
| **Tentar criar usuário com email duplicado** | `POST` | `/api/usuarios` | `{"nome": "Teste User 2", "email": "teste@email.com", "senha": "senha123"}` | **Status 409 Conflict** (ou similar, dependendo da implementação do tratamento de exceção). |
| **Listar todos os usuários** | `GET` | `/api/usuarios` | - | **Status 200 OK** com uma lista de usuários. |
| **Buscar usuário por ID** | `GET` | `/api/usuarios/{id}` | - | **Status 200 OK** com os dados do usuário. O `{id}` deve ser substituído por um ID de usuário válido. |
| **Buscar usuário com ID inexistente** | `GET` | `/api/usuarios/{id}` | - | **Status 404 Not Found**. O `{id}` deve ser substituído por um ID de usuário inválido. |
| **Deletar um usuário** | `DELETE` | `/api/usuarios/{id}` | - | **Status 204 No Content**. O `{id}` deve ser substituído por um ID de usuário válido. |
| **Tentar deletar usuário com ID inexistente** | `DELETE` | `/api/usuarios/{id}` | - | **Status 404 Not Found**. O `{id}` deve ser substituído por um ID de usuário inválido. |

### 3.2. API de Carteiras (`/api/carteiras`)

| Cenário | Método | Endpoint | Corpo da Requisição (JSON) | Resultado Esperado |
|---|---|---|---|---|
| **Buscar carteira por ID do usuário** | `GET` | `/api/carteiras/usuario/{usuarioId}` | - | **Status 200 OK** com os dados da carteira. O `{usuarioId}` deve ser substituído por um ID de usuário válido. |
| **Buscar carteira por ID da carteira** | `GET` | `/api/carteiras/{carteiraId}` | - | **Status 200 OK** com os dados da carteira. O `{carteiraId}` deve ser substituído por um ID de carteira válido. |

### 3.3. API de Investimentos (`/api/investimentos`)

| Cenário | Método | Endpoint | Corpo da Requisição (JSON) | Resultado Esperado |
|---|---|---|---|---|
| **Criar um novo investimento** | `POST` | `/api/investimentos/usuario/{usuarioId}` | `{"ticker": "PETR4", "valor": 5000, "dias": 30}` | **Status 200 OK** com o novo investimento no corpo da resposta. O `{usuarioId}` deve ser substituído por um ID de usuário válido. |
| **Listar investimentos por usuário** | `GET` | `/api/investimentos/usuario/{usuarioId}` | - | **Status 200 OK** com uma lista de investimentos. O `{usuarioId}` deve ser substituído por um ID de usuário válido. |
| **Listar investimentos por carteira** | `GET` | `/api/investimentos/carteira/{carteiraId}` | - | **Status 200 OK** com uma lista de investimentos. O `{carteiraId}` deve ser substituído por um ID de carteira válido. |
| **Buscar investimento por ID** | `GET` | `/api/investimentos/{id}` | - | **Status 200 OK** com os dados do investimento. O `{id}` deve ser substituído por um ID de investimento válido. |
| **Atualizar um investimento** | `PUT` | `/api/investimentos/{id}` | `{"ticker": "PETR4", "valor": 6000, "dias": 45}` | **Status 200 OK** com o investimento atualizado. O `{id}` deve ser substituído por um ID de investimento válido. |
| **Deletar um investimento** | `DELETE` | `/api/investimentos/{id}` | - | **Status 204 No Content**. O `{id}` deve ser substituído por um ID de investimento válido. |
| **Upload de arquivo de investimentos** | `POST` | `/api/investimentos/usuario/{usuarioId}/upload` | (multipart/form-data com um arquivo .txt) | **Status 200 OK** com um resumo do upload. O `{usuarioId}` deve ser substituído por um ID de usuário válido. |

### 3.4. API de Ativos (`/api/ativos`)

| Cenário | Método | Endpoint | Corpo da Requisição (JSON) | Resultado Esperado |
|---|---|---|---|---|
| **Buscar dados de um ativo** | `GET` | `/api/ativos/{codigo}` | - | **Status 200 OK** com os dados do ativo. O `{codigo}` deve ser um ticker válido, como `PETR4`. |
| **Buscar dados de um ativo inexistente** | `GET` | `/api/ativos/{codigo}` | - | **Status 404 Not Found**. O `{codigo}` deve ser um ticker inválido. |

### 3.5. Segurança

| Cenário | Método | Endpoint | Corpo da Requisição (JSON) | Resultado Esperado |
|---|---|---|---|---|
| **Acessar qualquer endpoint sem autenticação** | `GET` | `/api/usuarios` | - | **Status 401 Unauthorized**. |
| **Acessar qualquer endpoint com credenciais inválidas** | `GET` | `/api/usuarios` | - | **Status 401 Unauthorized**. |

---

## 4. Conclusão

Este roteiro de testes cobre as principais funcionalidades da API. É recomendado que os testes sejam automatizados em uma pipeline de CI/CD para garantir a qualidade contínua do software.
