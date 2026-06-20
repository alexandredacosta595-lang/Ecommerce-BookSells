/*
package com.mulemba.booksells.config;

import com.mulemba.booksells.model.*;
import com.mulemba.booksells.model.enums.*;
import com.mulemba.booksells.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final OrderRepository orderRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final LibraryItemRepository libraryItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        seedUsers();
        seedCategories();
        seedAuthors();
        seedBooks();
        seedReviews();
        */
/*seedPromoCodes();*//*

        seedOrders();
        seedWishlistAndLibrary();
    }

    private void seedUsers() {
        userRepository.saveAll(List.of(
                User.builder()
                        .name("Alexandre Da Costa")
                        .email("alexandredacosta595@gmail.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(UserRole.USER).userType(UserType.READER)
                        .avatar("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80")
                        .memberSince(LocalDate.of(2025, 6, 3))
                        .bio("Leitor ávido e entusiasta de livros físicos e digitais.")
                        .enabled(true).build(),
                User.builder()
                        .name("Cliente Demo")
                        .email("customer@bookverse.com")
                        .password(passwordEncoder.encode("customerPass12"))
                        .role(UserRole.USER).userType(UserType.READER)
                        .avatar("https://api.dicebear.com/7.x/adventurer/svg?seed=customer")
                        .memberSince(LocalDate.now()).enabled(true).build(),
                User.builder()
                        .name("Administrador")
                        .email("admin@bookverse.com")
                        .password(passwordEncoder.encode("adminPass55"))
                        .role(UserRole.ADMIN).userType(UserType.READER)
                        .avatar("https://api.dicebear.com/7.x/adventurer/svg?seed=admin")
                        .memberSince(LocalDate.now()).enabled(true).build()
        ));
    }

    private void seedCategories() {
        categoryRepository.saveAll(List.of(
                cat("cat-1", "Ficção e Literatura", "fiction", "BookOpen", 7),
                cat("cat-2", "Ciência e Tecnologia", "sci-tech", "Cpu", 3),
                cat("cat-3", "Desenvolvimento Pessoal", "self-improvement", "Sparkles", 3),
                cat("cat-4", "Negócios e Finanças", "business-finance", "TrendingUp", 3),
                cat("cat-5", "Biografia e História", "biography-history", "History", 3)
        ));
    }

    private Category cat(String id, String name, String slug, String icon, int count) {
        return Category.builder().name(name).slug(slug).iconName(icon).booksCount(count).build();
    }

    private void seedAuthors() {
        authorRepository.saveAll(List.of(
                author("aut-1", "Pepetela", "Renomado escritor angolano, vencedor do Prémio Camões.", 3),
                author("aut-2", "Mia Couto", "Escritor moçambicano de realismo mágico.", 1),
                author("aut-3", "Ondjaki", "Autor e poeta angolano.", 1),
                author("aut-4", "José Eduardo Agualusa", "Escritor e jornalista angolano.", 2),
                author("aut-5", "Agostinho Neto", "Primeiro Presidente de Angola e poeta.", 1),
                author("aut-6", "Manuel Rui", "Escritor seminal angolano.", 1),
                author("aut-7", "Dr. Stuart Russell", "Cientista de computação.", 2),
                author("aut-8", "James Clear", "Especialista em formação de hábitos.", 2),
                author("aut-9", "Robert Kiyosaki", "Investidor e autor financeiro.", 2),
                author("aut-10", "Nelson Mandela", "Líder histórico e ex-presidente da África do Sul.", 1)
        ));
    }

    private Author author(String id, String name, String bio, int count) {
        return Author.builder().name(name).bio(bio)
                .avatar("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150")
                .booksCount(count).build();
    }

    private void seedBooks() {
        bookRepository.saveAll(List.of(
                book("book-1", "Mayombe", "aut-1", "cat-1", 6500, 4.9, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 25, true, false, 142,
                        UserType.PUBLISHER, "Editora Chá de Caxinde"),
                book("book-2", "Terra Sonâmbula", "aut-2", "cat-1", 7200, 4.8, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 18, true, false, 96,
                        UserType.PUBLISHER, "Editora Mulemba"),
                book("book-3", "Bom Dia Camaradas", "aut-3", "cat-1", 5500, 4.7, BookType.PHYSICAL,
                        List.of(BookFormat.PHYSICAL), 8, false, false, 64,
                        UserType.BOOKSTORE, "Sebo do Sambizanga"),
                book("book-4", "O Vendedor de Passados", "aut-4", "cat-1", 6800, 4.8, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 14, true, false, 110,
                        UserType.READER, "António Luando"),
                book("book-5", "Quem Me Dera Ser Onda", "aut-6", "cat-1", 4200, 4.9, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF), 35, true, false, 185,
                        UserType.BOOKSTORE, "Livraria Kilamba"),
                book("book-6", "Sagrada Esperança", "aut-5", "cat-1", 4900, 4.9, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 50, true, false, 220,
                        UserType.PUBLISHER, "Editora UEA Luanda"),
                book("book-7", "O Legado de Njinga Mbandi", "aut-4", "cat-5", 8500, 4.8, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF), 15, false, false, 38,
                        UserType.PUBLISHER, "Distribuidora Mulemba"),
                book("book-8", "Caminho para a Liberdade", "aut-10", "cat-5", 11000, 4.9, BookType.PHYSICAL,
                        List.of(BookFormat.PHYSICAL), 6, true, false, 165,
                        UserType.BOOKSTORE, "Livraria Lello Angola"),
                book("book-9", "A Geração da Utopia", "aut-1", "cat-1", 7500, 4.7, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 12, false, true, 43,
                        UserType.AUTHOR, "Pepetela"),
                book("book-10", "Código Limpo (Clean Code)", "aut-7", "cat-2", 18500, 4.9, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 14, true, false, 310,
                        UserType.BOOKSTORE, "Livraria das Nações"),
                book("book-11", "Inteligência Artificial moderna", "aut-7", "cat-2", 26000, 4.9, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF), 8, true, false, 88,
                        UserType.PUBLISHER, "LTC Editora"),
                book("book-12", "Hábitos Atómicos", "aut-8", "cat-3", 9500, 4.8, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 45, true, false, 420,
                        UserType.PUBLISHER, "Editora Mulemba"),
                book("book-13", "O Poder do Hábito", "aut-8", "cat-3", 8900, 4.7, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 20, false, false, 198,
                        UserType.READER, "Teresa Samba"),
                book("book-14", "Pai Rico, Pai Pobre", "aut-9", "cat-4", 12000, 4.8, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 15, true, false, 512,
                        UserType.BOOKSTORE, "Livraria Sorte Comum"),
                book("book-15", "O Homem Mais Rico da Babilónia", "aut-9", "cat-4", 6000, 4.7, BookType.DIGITAL,
                        List.of(BookFormat.PDF, BookFormat.EPUB), 999, false, false, 145,
                        UserType.AUTHOR, "Robert Kiyosaki"),
                book("book-16", "O Cão e os Caluandas", "aut-1", "cat-1", 6805, 4.8, BookType.BOTH,
                        List.of(BookFormat.PHYSICAL, BookFormat.PDF, BookFormat.EPUB), 20, false, true, 43,
                        UserType.PUBLISHER, "Editora Mulemba")
        ));
    }

    private Book book(String id, String title, String authorId, String categoryId, double price, double rating,
                      BookType type, List<BookFormat> formats, int stock, boolean bestSeller, boolean newRelease,
                      int reviewsCount, UserType sellerType, String sellerName) {
        return Book.builder()
                .id(id).title(title).authorId(authorId).categoryId(categoryId)
                .description("Descrição literária de " + title + " — obra disponível na Livraria Mulemba.")
                .price(BigDecimal.valueOf(price)).rating(rating)
                .coverImage("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=450")
                .coverColor("from-blue-600 via-indigo-900 to-amber-950")
                .type(type).formats(formats).stock(stock).pages(240)
                .publishedDate(LocalDate.of(2000, 1, 1))
                .bestSeller(bestSeller).newRelease(newRelease).reviewsCount(reviewsCount)
                .isbn("978-972-21-0814-1").publisher("Editora Mulemba")
                .sellerType(sellerType).sellerName(sellerName)
                .build();
    }

    private void seedReviews() {
        reviewRepository.saveAll(List.of(
                review("rev-1", "book-1", "usr-412", "Kalandula Santos", 5, "Uma leitura absolutamente obrigatória."),
                review("rev-2", "book-1", "usr-customer", "Mariana Benguela", 4, "Muito expressivo e actual."),
                review("rev-3", "book-10", "usr-412", "Pedro Mateus", 5, "Este livro mudou a minha forma de programar."),
                review("rev-4", "book-12", "usr-customer", "Cláudia Huambo", 5, "Método brilhante de hábitos."),
                review("rev-5", "book-16", "usr-412", "Afonso Luanda", 5, "Maravilhoso! Edição impecável.")
        ));
    }

    private Review review(String id, String bookId, String userId, String userName, int rating, String comment) {
        return Review.builder().bookId(bookId).userId(userId).userName(userName)
                .rating(rating).comment(comment).date(LocalDate.now()).build();
    }
*/
/*
    private void seedPromoCodes() {
        promoCodeRepository.saveAll(List.of(
                PromoCode.builder().code("MULEMBA20").discountValue(new BigDecimal("0.20")).percentage(true).active(true).build(),
                PromoCode.builder().code("BOOKVERSE20").discountValue(new BigDecimal("0.20")).percentage(true).active(true).build(),
                PromoCode.builder().code("BEMVINDO10").discountValue(new BigDecimal("0.10")).percentage(true).active(true).build(),
                PromoCode.builder().code("LEITURA5").discountValue(new BigDecimal("5000")).percentage(false).active(true).build()
        ));
    }*//*


    private void seedOrders() {
        Order order1 = Order.builder().userId("usr-412")
                .subtotal(new BigDecimal("37.95")).discount(new BigDecimal("5.00"))
                .shippingCharge(new BigDecimal("4.99")).tax(new BigDecimal("2.63")).total(new BigDecimal("40.57"))
                .date(LocalDate.of(2026, 5, 15)).status(OrderStatus.DELIVERED)
                .paymentMethod(PaymentMethod.CARD).trackingNumber("TRK-89210291-US")
                .shippingAddress(Order.ShippingAddress.builder()
                        .fullName("Alexandre Da Costa").street("Avenida Comandante Valódia, nº 45")
                        .city("Luanda").state("LUA").zipCode("0000").country("Angola").phone("+244 923 456 789").build())
                .items(new java.util.ArrayList<>()).build();

        order1.getItems().add(OrderItem.builder().order(order1).bookId("book-4")
                .title("O Vendedor de Passados").price(new BigDecimal("6800")).quantity(1)
                .selectedFormat(BookFormat.PHYSICAL).build());
        order1.getItems().add(OrderItem.builder().order(order1).bookId("book-13")
                .title("O Poder do Hábito").price(new BigDecimal("8900")).quantity(1)
                .selectedFormat(BookFormat.EPUB).build());

        Order order2 = Order.builder()
                .userId("usr-412")
                .subtotal(new BigDecimal("7200")).discount(BigDecimal.ZERO)
                .shippingCharge(BigDecimal.ZERO).tax(new BigDecimal("576")).total(new BigDecimal("7776"))
                .date(LocalDate.of(2026, 6, 2)).status(OrderStatus.PROCESSING)
                .paymentMethod(PaymentMethod.CARD)
                .shippingAddress(Order.ShippingAddress.builder()
                        .fullName("Alexandre Da Costa").street("Avenida Comandante Valódia, nº 45")
                        .city("Luanda").state("LUA").zipCode("0000").country("Angola").phone("+244 923 456 789").build())
                .items(new java.util.ArrayList<>()).build();

        order2.getItems().add(OrderItem.builder().order(order2).bookId("book-2")
                .title("Terra Sonâmbula").price(new BigDecimal("7200")).quantity(1)
                .selectedFormat(BookFormat.PDF).build());

        orderRepository.saveAll(List.of(order1, order2));
    }

    private void seedWishlistAndLibrary() {
        wishlistItemRepository.saveAll(List.of(
                WishlistItem.builder().userId("usr-412").bookId("book-1").build(),
                WishlistItem.builder().userId("usr-412").bookId("book-4").build(),
                WishlistItem.builder().userId("usr-412").bookId("book-11").build()
        ));

        libraryItemRepository.saveAll(List.of(
                LibraryItem.builder().userId("usr-412").bookId("book-2").progress(45)
                        .format(BookFormat.PDF).lastRead(LocalDate.of(2026, 6, 2)).downloaded(true).build(),
                LibraryItem.builder().userId("usr-412").bookId("book-8").progress(12)
                        .format(BookFormat.EPUB).lastRead(LocalDate.of(2026, 5, 18)).downloaded(false).build(),
                LibraryItem.builder().userId("usr-412").bookId("book-13").progress(85)
                        .format(BookFormat.EPUB).lastRead(LocalDate.of(2026, 6, 3)).downloaded(true).build()
        ));
    }
}
*/
