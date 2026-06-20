# BookSells API — Livraria Mulemba

API REST em **Spring Boot** para e-commerce de livros físicos e digitais, integrada ao frontend React (Vite).

## Requisitos

- Java 21
- Maven 3.9+
- Node.js 18+ (frontend)

## Executar o backend

```bash
cd sistema-api
mvn spring-boot:run
```

API disponível em `http://localhost:8080/api`

Console H2: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:mem:booksells`, user: `sa`, password vazio)

## Executar o frontend

```bash
cd frontend   # ou o caminho do projeto React
npm install
npm run dev
```

Frontend em `http://localhost:3000` — configure `VITE_API_URL=http://localhost:8080/api` no `.env`

## Credenciais de teste

| Perfil | E-mail | Senha |
|--------|--------|-------|
| Cliente | `alexandredacosta595@gmail.com` | `password123` |
| Cliente demo | `customer@bookverse.com` | `customerPass12` |
| Admin | `admin@bookverse.com` | `adminPass55` |

## Endpoints principais

| Método | Endpoint | Auth | Descrição |
|--------|----------|------|-----------|
| POST | `/api/auth/register` | — | Registo de utilizador |
| POST | `/api/auth/login` | — | Login (retorna JWT) |
| GET | `/api/auth/me` | JWT | Perfil do utilizador |
| GET | `/api/books` | — | Catálogo com filtros e paginação |
| GET | `/api/categories` | — | Categorias |
| GET | `/api/authors` | — | Autores |
| GET | `/api/reviews/book/{id}` | — | Avaliações de um livro |
| POST | `/api/reviews/book/{id}` | JWT | Criar avaliação |
| GET/POST | `/api/wishlist` | JWT | Lista de desejos |
| GET | `/api/library` | JWT | Biblioteca digital |
| POST | `/api/orders` | JWT | Finalizar compra |
| POST | `/api/coupons/validate` | — | Validar cupom |
| POST/PUT/DELETE | `/api/books` | Admin | CRUD de livros |
| GET | `/api/admin/stats` | Admin | Estatísticas |

## Autenticação

- JWT stateless com BCrypt (strength 12)
- Token enviado no header `Authorization: Bearer <token>`
- Rotas públicas: catálogo, autores, categorias, login/registo
- Rotas admin protegidas com role `ADMIN`

## Cupons de teste

- `MULEMBA20` — 20% desconto
- `BEMVINDO10` — 10% desconto
- `LEITURA5` — 5000 Kz desconto fixo
