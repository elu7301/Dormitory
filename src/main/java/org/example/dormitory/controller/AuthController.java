package org.example.dormitory.controller;

import org.example.dormitory.entity.Role;
import org.example.dormitory.entity.User;
import org.example.dormitory.repository.RoleRepository;
import org.example.dormitory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * Контроллер для управления аутентификацией и регистрацией пользователей.
 */
@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Отображает форму регистрации нового пользователя.
     *
     * @param model модель для передачи данных на страницу.
     * @return название представления для формы регистрации.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Отображает страницу входа в систему.
     *
     * @return название представления для страницы входа.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Обрабатывает регистрацию нового пользователя.
     *
     * @param user   объект пользователя, переданный из формы регистрации.
     * @param result объект для проверки ошибок валидации.
     * @param model  модель для передачи данных на страницу.
     * @return перенаправление на страницу входа или на страницу регистрации в случае ошибки.
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Имя пользователя уже существует");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Роль USER не найдена"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "redirect:/login?registerSuccess";
    }
}
