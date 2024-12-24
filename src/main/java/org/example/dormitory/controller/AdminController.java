package org.example.dormitory.controller;

import org.example.dormitory.entity.Role;
import org.example.dormitory.entity.User;
import org.example.dormitory.repository.RoleRepository;
import org.example.dormitory.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Контроллер для управления пользователями и ролями администратора.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Конструктор класса AdminController.
     *
     * @param userRepository репозиторий для управления пользователями
     * @param roleRepository репозиторий для управления ролями
     */
    public AdminController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Обрабатывает запрос для отображения списка пользователей.
     *
     * @param model объект Model для передачи данных в представление
     * @return имя представления для отображения списка пользователей
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    /**
     * Обрабатывает запрос для изменения роли пользователя.
     *
     * @param id       идентификатор пользователя
     * @param roleName имя роли для назначения
     * @return перенаправление на страницу со списком пользователей
     */
    @PostMapping("/users/{id}/toggleRole")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleRole(@PathVariable("id") Long id, @RequestParam("roleName") String roleName) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + roleName));

        Set<Role> roles = new HashSet<>();
        if ("ADMIN".equals(roleName)) {
            roles.add(role);
        } else {
            roles.add(roleRepository.findByName("USER").orElseThrow(() -> new IllegalArgumentException("Role USER not found")));
        }
        user.setRoles(roles);
        userRepository.save(user);

        return "redirect:/admin/users";
    }
}
