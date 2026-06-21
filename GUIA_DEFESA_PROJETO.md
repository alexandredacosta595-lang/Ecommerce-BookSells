# 🎓 Guia de Defesa do Projeto: Livraria Mulemba

Este documento serve como um roteiro completo para a defesa do seu projeto **Livraria Mulemba** (E-commerce de Livros). Ele detalha de forma técnica, mas compreensível, as decisões arquiteturais, tecnologias e segurança utilizadas para construir o sistema. Você pode usá-lo como base para os slides e para o discurso de apresentação.

---

## 1. Visão Geral do Projeto (O problema e a solução)

*   **O que é?** A Livraria Mulemba é uma plataforma completa de comércio eletrônico (e-commerce) focada na venda de livros físicos e digitais (e-books).
*   **Problema que resolve:** Moderniza e facilita o acesso à literatura, digitalizando o processo de compra de livros e disponibilizando uma "estante virtual" segura para a leitura de e-books, além de entregar um sistema robusto de gestão de estoque para a administração.

---

## 2. Requisitos do Sistema

Durante a defesa, deixe claro que o escopo foi bem definido através dos seguintes requisitos:

### Requisitos Funcionais (O que o sistema faz)
*   **Gestão de Usuários:** Cadastro, login seguro e painel do usuário para visualização de faturas.
*   **Catálogo e Vendas:** Navegação por categorias, visualização de detalhes do livro, carrinho de compras inteligente e processo de checkout com controle de pedidos.
*   **Biblioteca Digital:** Leitor integrado exclusivo para os e-books adquiridos, garantindo proteção de conteúdo.
*   **Painel Administrativo:** Controle de acessos por tipo de usuário (RBAC - Role Based Access Control) para gestão de inventário, aprovação de pedidos e categorias.

### Requisitos Não Funcionais (Atributos de qualidade e como o sistema se comporta)
*   **Desempenho:** Carregamento imediato sem recarregar a página (SPA com React).
*   **Responsividade:** Interface adaptável 100% a celulares, tablets e computadores (Mobile-First via Tailwind CSS).
*   **Segurança e Privacidade:** Autenticação via tokens JWT, senhas cifradas e bloqueio de download/acesso direto a arquivos protegidos.
*   **Escalabilidade:** Arquitetura desacoplada onde Frontend e Backend são projetos completamente separados comunicando-se via API.

---

## 3. Banco de Dados

*   **Tecnologia:** PostgreSQL (via Neon DB).
*   **Por que relacional?** Porque e-commerces lidam com dados financeiros (pedidos, itens do pedido, usuários). Bancos de dados relacionais garantem **Transações ACID**, ou seja, se um pagamento falha, o banco reverte (rollback) a baixa do estoque automaticamente.
*   **Estrutura principal:**
    *   `Users`: Dados de login, roles (permissões).
    *   `Books` / `Categories`: Estrutura de produtos.
    *   `Orders` / `OrderItems`: Vinculam o usuário, produto, e registram os dados imutáveis da compra.
*   **Mapeamento:** Utilizamos Spring Data JPA (Hibernate) no Backend, abstraindo as tabelas SQL para Classes/Objetos no Java.

---

## 4. Backend (Motor do Sistema)

*   **Linguagem & Framework:** Java 21 com Spring Boot 3.4.1.
*   **Por que Spring Boot?** É o padrão ouro no mercado para criar APIs RESTful corporativas. Ele é seguro, escalável e de fácil manutenção.
*   **Arquitetura (MVC e Camadas):**
    *   **Controllers:** Responsáveis por receber as requisições HTTP (`GET`, `POST`, `PUT`, `DELETE`).
    *   **Services:** Onde reside a "inteligência" (Regras de Negócio). O cálculo do carrinho, verificação de estoque e validação do e-book é feito aqui.
    *   **Repositories:** Responsáveis apenas pela comunicação com o banco de dados.
*   **Gestão de Arquivos:** Uploads (imagens de capas e arquivos em PDF) são manipulados por métodos do Spring Boot e armazenados no disco do servidor, garantindo isolamento.

---

## 5. Frontend (A Interface e Experiência do Usuário)

A interface foi projetada para ter apelo visual "Premium", fundamental para reter clientes em um e-commerce.

*   **Tecnologias:** React 19, TypeScript, construído em cima do Vite.
*   **Estilização:** Tailwind CSS 4. Adotamos o Tailwind ao invés de CSS puro para garantir padronização rápida do Design System e facilidade para ajustar tamanhos de tela.
*   **Gerenciamento de Estado Global:** Zustand.
    *   *Se perguntarem por que não o Redux:* "Zustand resolve nosso problema do Carrinho de Compras de forma mais performática, com bem menos linhas de código e menos verbosidade, mantendo a simplicidade da aplicação".
*   **Roteamento:** React Router DOM (navegação instantânea SPA).
*   **Segurança de Tipos:** O uso de TypeScript garante que não ocorram "erros de campos vazios" ou que a interface quebre por receber dados em formato incorreto do Backend.

---

## 6. Segurança (Ponto de Ouro da Defesa)

Bancas de avaliação sempre questionam a segurança, especialmente em sistemas com transações e usuários.

*   **Autenticação JWT (JSON Web Tokens):** Não guardamos sessão no servidor. O Spring Security gera um token JWT. O frontend guarda esse token e o envia a cada requisição. O token expira e não pode ser falsificado.
*   **Criptografia de Senhas:** Nenhuma senha é salva em texto plano. Usamos o algoritmo de hash `BCrypt`. Nem mesmo os administradores do banco de dados sabem a senha do usuário.
*   **Autorização e Controle de Acesso:** Rotas administrativas do Backend só respondem se o token contiver uma *role* de Administrador/Gerente.
*   **Anti-Pirataria e Proteção de Ativos:** E-books NÃO ficam armazenados em pastas do frontend. Estão guardados num disco privado no Backend. Apenas usuários logados que **possuem o pedido pago daquele livro** conseguem abrir o leitor de PDF (que requisita o arquivo sob demanda via API restrita com verificação de autorização).
*   **CORS:** A API recusa requisições de sites de terceiros, autorizando apenas o domínio do nosso frontend.

---

## 7. Infraestrutura e Deploy (O Sistema no Ar)

Para provar que o projeto está em um fluxo de vida real (Produção), nós o publicamos.

*   **Frontend (Vercel):** Foi configurado o *Continuous Deployment* (CD). A Vercel (provedor oficial das tecnologias web modernas) observa nosso repositório no GitHub; sempre que enviamos uma atualização, ela faz o build otimizado automático (`vercel.json` configurado para rewrites do SPA).
*   **Backend (Render & Docker):**
    *   Nós **conteinerizamos** a aplicação Java criando um arquivo `Dockerfile` (arquitetura em 2 estágios: Build e Run). Isso garante que o nosso código "rode igual" em qualquer máquina ou provedor de nuvem.
    *   Hospedamos na plataforma Render usando um disco persistente virtual (`/data/uploads`). Diferente de nuvens temporárias (como Heroku do passado), nosso disco persistente garante que os livros armazenados nunca sejam apagados nas reinicializações do servidor.

---

## 💡 Dicas Finais para o Dia da Apresentação

1.  **Apresente de Forma Prática (O Fluxo de Valor):** Em vez de ficar mostrando telas paradas, simule o caminho que dá lucro. Crie um usuário ao vivo -> Navegue -> Coloque no Carrinho -> "Pague" -> Mostre a mudança de estoque no admin -> Abra o livro digital na área de leitura.
2.  **Esteja preparado para os Desafios Superados:** Sempre perguntam "qual foi a maior dificuldade". Mencione: "Assegurar que a regra de autorização entre o Spring Security e a leitura controlada de PDFs (para não permitir cópia indiscriminada) conversasse perfeitamente com a autenticação no React".
3.  **Valorize o Desempenho:** Destaque a rapidez que os livros carregam por não precisarem recarregar a página (vantagem do React + Vite + Typescript).
