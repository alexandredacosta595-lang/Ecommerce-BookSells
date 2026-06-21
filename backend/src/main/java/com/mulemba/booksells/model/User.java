package com.mulemba.booksells.model;

import com.mulemba.booksells.model.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "utilizadores")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utilizadores_perfis",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

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
