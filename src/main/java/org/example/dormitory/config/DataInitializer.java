package org.example.dormitory.config;

import org.example.dormitory.entity.Role;
import org.example.dormitory.entity.User;
import org.example.dormitory.repository.RoleRepository;
import org.example.dormitory.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс для инициализации данных в базе данных при запуске приложения.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор класса DataInitializer.
     *
     * @param roleRepository репозиторий для управления ролями
     * @param userRepository репозиторий для управления пользователями
     * @param passwordEncoder кодировщик паролей
     */
    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Метод, выполняющий инициализацию данных в базе данных.
     * Создает роли "USER" и "ADMIN", если их нет, и добавляет администратора с заданными учетными данными.
     *
     * @param args аргументы командной строки
     */
    @Override
    public void run(String... args) {
        Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
            Role role = new Role();
            role.setName("USER");
            return roleRepository.save(role);
        });

        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ADMIN");
            return roleRepository.save(role);
        });

        if (userRepository.findByUsername("DimaK").isEmpty()) {
            User admin = new User();
            admin.setUsername("DimaK");
            admin.setPassword(passwordEncoder.encode("7301"));
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            admin.setRoles(roles);
            userRepository.save(admin);
        }
    }
}
