package com.mulemba.booksells.model;

import com.mulemba.booksells.model.enums.UserRole;
import com.mulemba.booksells.model.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String avatar;
    private String bio;
    private String companyName;
    private String city;
    private String state;
    private String phone;
    private String whatsapp;
    private String website;

    @Column(nullable = false)
    private LocalDate memberSince;

    @Column(nullable = false)
    private boolean enabled = true;
}
