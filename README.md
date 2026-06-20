# Livraria Mulemba - E-commerce de Livros (Ecommerce-BookSells)

## Sobre o Projeto
**Livraria Mulemba** é uma plataforma completa de e-commerce voltada para a venda de livros físicos e digitais (e-books). O sistema permite a exploração de um vasto catálogo de obras, gestão de carrinho de compras, finalização de pedidos (checkout), além de contar com um painel administrativo robusto para a gestão da loja e uma biblioteca digital para os usuários lerem seus e-books adquiridos.

## 🚀 Funcionalidades Principais

### Para Clientes (Usuários)
*   **Autenticação e Perfil:** Cadastro, Login (via JWT) e gestão da conta do usuário.
*   **Catálogo de Livros:** Navegação intuitiva por categorias, visualização de autores e busca rápida.
*   **Detalhes e Avaliações:** Página dedicada para cada livro com sinopse, preço, estoque e sistema de avaliações (Reviews).
*   **Carrinho e Checkout:** Adição/remoção de itens, aplicação de cupons de desconto (PromoCodes) e processo de finalização de compra.
*   **Painel do Usuário (Dashboard):** Histórico de pedidos e detalhes das compras anteriores.
*   **Biblioteca Digital:** Área exclusiva para acessar e ler os e-books e conteúdos digitais comprados.
*   **Lista de Desejos (Wishlist):** Salvamento de livros favoritos para compras futuras.

### Para Administradores
*   **Dashboard Administrativo:** Visão geral de vendas, usuários ativos e status da loja.
*   **Gestão de Catálogo:** Adição, edição e remoção de livros, categorias e autores.
*   **Gestão de Pedidos:** Acompanhamento e atualização de status das compras dos clientes.
*   **Controle de Promoções:** Criação e gestão de cupons de desconto e campanhas promocionais.

---

## 🛠️ Tecnologias Utilizadas

### Backend (API REST)
A API foi desenvolvida com foco em segurança, escalabilidade e performance.
*   **Linguagem:** Java 21
*   **Framework:** Spring Boot 3.4.1
*   **Persistência de Dados:** Spring Data JPA / Hibernate
*   **Banco de Dados:** MySQL
*   **Segurança & Autenticação:** Spring Security com JWT (JSON Web Tokens)
*   **Outras Bibliotecas:** Lombok (redução de boilerplate), Spring Validation

### Frontend (SPA)
A interface de usuário foi criada para oferecer uma experiência moderna, rápida e responsiva.
*   **Linguagem:** TypeScript
*   **Biblioteca Principal:** React 19
*   **Build & Bundler:** Vite
*   **Estilização e UI:** Tailwind CSS 4
*   **Gerenciamento de Estado:** Zustand
*   **Roteamento:** React Router DOM
*   **Requisições HTTP:** Axios
*   **Ícones e Animações:** Lucide React e Motion

---

## 📁 Estrutura do Projeto

O repositório é organizado no formato de *monorepo* conceitual (dividido em pastas distintas para cliente e servidor):

*   **`/backend`**: Contém todo o código-fonte da API em Java/Spring Boot (Controllers, Services, Repositories, Models/Entities e DTOs).
*   **`/frontend`**: Contém o aplicativo React, incluindo as páginas (Pages), componentes reutilizáveis (Components), integração com a API (Services) e gestão de estados (Store).

---

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
Certifique-se de ter instalado em sua máquina:
*   [Java 21](https://jdk.java.net/21/)
*   [Maven](https://maven.apache.org/)
*   [Node.js](https://nodejs.org/) (Versão 18 ou superior)
*   Servidor [MySQL](https://www.mysql.com/)

### 1. Rodando o Backend (API)
1. Abra um terminal e navegue até a pasta raiz do backend:
   ```bash
   cd backend
   ```
2. Crie um banco de dados no MySQL para o projeto (ex: `mulemba_db`).
3. Configure as credenciais do banco de dados em `src/main/resources/application.properties` (ou `application.yml`).
4. Execute a aplicação usando o Maven:
   ```bash
   mvn spring-boot:run
   ```
   > A API estará rodando por padrão em `http://localhost:8080`.

### 2. Rodando o Frontend (Interface)
1. Abra um novo terminal e navegue até a pasta do frontend:
   ```bash
   cd frontend
   ```
2. Instale as dependências do projeto:
   ```bash
   npm install
   ```
3. (Opcional) Se houver, copie o arquivo `.env.example` para `.env` e configure a URL base da API.
4. Inicie o servidor de desenvolvimento:
   ```bash
   npm run dev
   ```
   > Acesse a aplicação no seu navegador através de `http://localhost:3000`.

