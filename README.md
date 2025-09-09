# Livros API — Spring Boot

Projeto acadêmico com uma API REST para cadastrar e consultar **Livros**.

## Como executar

### Requisitos
- **Java 21** (ou 17)
- **Maven 3.9+** (ou use o Maven Wrapper do projeto)

### Rodando com Maven Wrapper
No diretório do projeto (pasta que contém o `pom.xml`):

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A aplicação sobe por padrão em **http://localhost:8080**.

### Banco H2 em memória
A API usa **H2 (in‑memory)**; ajuste em `src/main/resources/application.yml` (ou `application.properties`). Exemplo em YAML:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:livros
    username: sa
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  h2:
    console:
      enabled: true
      path: /h2-console
```

- Console do H2: **http://localhost:8080/h2-console**
- **JDBC URL**: `jdbc:h2:mem:livros` (ou `jdbc:h2:mem:testdb`, conforme seu `application.*`)
- **User**: `sa`
- **Password**: _(vazio)_

> Observação: por ser **em memória**, os dados são perdidos ao parar a aplicação.

---

## Modelo de dados (`Livro`)

Campos principais da entidade (ajuste conforme seu código real):
- `id` (Long ou UUID; se for UUID, use `String` no DTO)
- `titulo` (String)
- `autor` (String)
- `editora` (String)

Exemplo de objeto:
```json
{
  "id": 1,
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "editora": "Rocco"
}
```

---

## Rotas (Controllers)

Base path do recurso: **`/livros`** (se o seu controlador usar `/livraria`, basta trocar nos exemplos)

### 1) Criar livro
**POST** `/livros`

**Request (JSON):**
```json
{
  "titulo": "Capitães da Areia",
  "autor": "Jorge Amado",
  "editora": "Rocco"
}
```

**Respostas esperadas:**
- `201 Created` (ou `200 OK`, dependendo da implementação) — livro criado.
- Corpo: objeto salvo com `id` preenchido.

**cURL:**
```bash
curl -X POST "http://localhost:8080/livros"   -H "Content-Type: application/json"   -d '{
        "titulo": "Capitães da Areia",
        "autor": "Jorge Amado",
        "editora": "Rocco"
      }'
```

---

### 2) Buscar por ID
**GET** `/livros/{id}`

**Response 200 (JSON):**
```json
{
  "id": 1,
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "editora": "Rocco"
}
```

Possíveis respostas:
- `200 OK` — encontrado.
- `404 Not Found` — caso o ID não exista.

**cURL:**
```bash
curl -X GET "http://localhost:8080/livros/1"
```

---

### 3) Atualizar por ID
**PUT** `/livros/{id}`

**Request (JSON):**
```json
{
  "titulo": "Capitães da Areia (Edição Revisada)",
  "autor": "Jorge Amado",
  "editora": "Rocco"
}
```

Respostas:
- `200 OK` (ou `204 No Content`) — atualizado.
- `404 Not Found` — se não existir.

**cURL:**
```bash
curl -X PUT "http://localhost:8080/livros/1"   -H "Content-Type: application/json"   -d '{
        "titulo": "Capitães da Areia (Edição Revisada)",
        "autor": "Jorge Amado",
        "editora": "Rocco"
      }'
```

---

### 4) Atualização parcial
**PATCH** `/livros/{id}`

**Request (JSON):**
```json
{ "titulo": "Capitães da Areia (Edição 2024)" }
```

Respostas:
- `200 OK` — atualizado.
- `404 Not Found` — se não existir.

**cURL:**
```bash
curl -X PATCH "http://localhost:8080/livros/1"   -H "Content-Type: application/json"   -d '{ "titulo": "Capitães da Areia (Edição 2024)" }'
```

---

### 5) Excluir por ID
**DELETE** `/livros/{id}`

Respostas:
- `204 No Content` — excluído.
- `404 Not Found` — se não existir.

**cURL:**
```bash
curl -X DELETE "http://localhost:8080/livros/1"
```

---

### 6) Buscar por filtros (lista)
**GET** `/livros?titulo={valor}&autor={valor}&genero={valor}&q={texto}`

Exemplos:
```
GET /livros?titulo=Dom%20Casmurro
GET /livros?autor=Machado
GET /livros?editora=Rocco
GET /livros?q=machado%20rocco
```

**Response 200 (JSON):**
```json
[
  {
    "id": 1,
    "titulo": "Dom Casmurro",
    "autor": "Machado de Assis",
    "editora": "Rocco"
  },
  {
    "id": 2,
    "titulo": "Memórias Póstumas de Brás Cubas",
    "autor": "Machado de Assis",
    "editora": "Rocco"
  }
]
```

**cURL:**
```bash
curl -X GET "http://localhost:8080/livros?genero=Romance&autor=Machado&q=casmurro"
```

---

## Observações

- O ID pode ser **Long** (auto‑incremento) ou **UUID** gerado no backend (ajuste o README conforme sua entidade/DTO).
- O banco é **H2 em memória** (dados não persistem entre execuções).
- Para respostas padronizadas (`201 Created` com corpo, `404` para não encontrado, mensagens de validação), recomenda-se usar **DTOs com validação** (`jakarta.validation`) e um `@RestControllerAdvice` para tratamento uniforme de erros.
- Se a base path real for **`/livraria`**, substitua `/livros` nos exemplos.

---

## Estrutura sugerida do projeto

```
src/
├─ main/
│  ├─ java/
│  │  └─ com/exemplo/meuProjeto/
│  │     ├─ MeuProjetoApplication.java
│  │     ├─ controller/
│  │     │  └─ LivrariaController.java
│  │     ├─ model/
│  │     │  └─ Livro.java (ou Livraria.java)
│  │     └─ repository/
│  │        └─ LivroRepository.java (ou LivrariaRepository.java)
│  └─ resources/
│     ├─ application.yml
│     └─ data.sql (opcional para carga inicial)
└─ test/
```

