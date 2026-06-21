package com.mulemba.booksells.config;

import com.mulemba.booksells.model.Permission;
import com.mulemba.booksells.model.Role;
import com.mulemba.booksells.model.User;
import com.mulemba.booksells.repository.PermissionRepository;
import com.mulemba.booksells.repository.RoleRepository;
import com.mulemba.booksells.repository.UserRepository;
import com.mulemba.booksells.repository.CategoryRepository;
import com.mulemba.booksells.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RolePermissionInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Inicializando Roles e Permissions...");

        // 1. Create Permissions
        Permission readBooks = createPermissionIfNotFound("READ_BOOKS");
        Permission manageBooks = createPermissionIfNotFound("MANAGE_BOOKS");
        Permission manageUsers = createPermissionIfNotFound("MANAGE_USERS");
        Permission manageOrders = createPermissionIfNotFound("MANAGE_ORDERS");

        // 2. Create Roles and assign permissions
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", Set.of(readBooks, manageBooks, manageUsers, manageOrders));
        Role sellerRole = createRoleIfNotFound("ROLE_SELLER", Set.of(readBooks, manageBooks, manageOrders));
        Role userRole = createRoleIfNotFound("ROLE_USER", Set.of(readBooks));

        // 3. Assign roles to existing users if they don't have any
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                if (user.getEmail().equalsIgnoreCase("admin@gmail.com")) {
                    user.setRoles(new java.util.HashSet<>(Set.of(adminRole)));
                } else if (user.getUserType() != com.mulemba.booksells.model.enums.UserType.READER) {
                    user.setRoles(new java.util.HashSet<>(Set.of(sellerRole)));
                } else {
                    user.setRoles(new java.util.HashSet<>(Set.of(userRole)));
                }
                userRepository.save(user);
            }
        }
        
        // 4. Create default admin if not exists
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = User.builder()
                    .name("Administrador")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(new java.util.HashSet<>(Set.of(adminRole)))
                    .userType(com.mulemba.booksells.model.enums.UserType.PUBLISHER)
                    .avatar("https://api.dicebear.com/7.x/bottts/svg?seed=admin")
                    .memberSince(java.time.LocalDate.now())
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            log.info("Admin user created: admin@gmail.com / admin123");
        }
        
        // 5. Seed Categories
        if (categoryRepository.count() < 20) {
            log.info("Semeando Categorias...");
            java.util.Map<String, String> categoriesToSeed = new java.util.LinkedHashMap<>();
            categoriesToSeed.put("ROMANCE", "Romance");
            categoriesToSeed.put("FICCAO_CIENTIFICA", "Ficção Científica");
            categoriesToSeed.put("FANTASIA", "Fantasia");
            categoriesToSeed.put("TERROR", "Terror");
            categoriesToSeed.put("SUSPENSE", "Suspense");
            categoriesToSeed.put("BIOGRAFIA", "Biografia");
            categoriesToSeed.put("HISTORIA", "História");
            categoriesToSeed.put("FILOSOFIA", "Filosofia");
            categoriesToSeed.put("PSICOLOGIA", "Psicologia");
            categoriesToSeed.put("INFORMATICA", "Informática");
            categoriesToSeed.put("DIREITO", "Direito");
            categoriesToSeed.put("MEDICINA", "Medicina");
            categoriesToSeed.put("ECONOMIA", "Economia");
            categoriesToSeed.put("EMPREENDEDORISMO", "Empreendedorismo");
            categoriesToSeed.put("AUTOAJUDA", "Autoajuda");
            categoriesToSeed.put("POESIA", "Poesia");
            categoriesToSeed.put("LITERATURA_ANGOLANA", "Literatura Angolana");
            categoriesToSeed.put("INFANTIL", "Infantil");
            categoriesToSeed.put("JUVENIL", "Juvenil");
            categoriesToSeed.put("BANDA_DESENHADA", "Banda Desenhada");
            categoriesToSeed.put("DESPORTO", "Desporto");
            categoriesToSeed.put("RELIGIAO", "Religião");
            
            for (java.util.Map.Entry<String, String> entry : categoriesToSeed.entrySet()) {
                Category cat = Category.builder()
                        .name(entry.getValue())
                        .slug(entry.getKey().toLowerCase())
                        .iconName("BookOpen")
                        .booksCount(0)
                        .build();
                categoryRepository.save(cat);
            }
        }
        
        log.info("Roles e Permissions inicializadas com sucesso.");
    }

    private Permission createPermissionIfNotFound(String name) {
        Optional<Permission> permission = permissionRepository.findByName(name);
        if (permission.isEmpty()) {
            Permission newPermission = new Permission();
            newPermission.setName(name);
            return permissionRepository.save(newPermission);
        }
        return permission.get();
    }

    private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(name);
            newRole.setPermissions(permissions);
            return roleRepository.save(newRole);
        }
        return role.get();
    }
}
