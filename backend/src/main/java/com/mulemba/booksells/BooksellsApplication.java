package com.mulemba.booksells;

import com.mulemba.booksells.model.User;
import com.mulemba.booksells.model.enums.UserRole;
import com.mulemba.booksells.model.enums.UserType;
import com.mulemba.booksells.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class BooksellsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksellsApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@bookverse.com")) {
                User admin = User.builder()
                        .name("Administrador")
                        .email("admin@bookverse.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(UserRole.ADMIN)
                        .userType(UserType.READER)
                        .memberSince(LocalDate.now())
                        .enabled(true)
                        .build();
                userRepository.save(admin);
                System.out.println("Admin user created: admin@bookverse.com / admin123");
            }
        };
    }
}
