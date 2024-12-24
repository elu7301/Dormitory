package org.example.dormitory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Сущность, представляющая пользователя системы.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя. Поле обязательно для заполнения и должно быть уникальным.
     */
    @NotBlank(message = "Имя пользователя обязательно")
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Пароль пользователя. Поле обязательно для заполнения.
     */
    @NotBlank(message = "Пароль обязателен")
    @Column(nullable = false)
    private String password;

    /**
     * Роли, назначенные пользователю. Связь с сущностью {@link Role}.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
