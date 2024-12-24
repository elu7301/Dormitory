package org.example.dormitory.entity;

import jakarta.persistence.*;
import java.util.Set;

/**
 * Сущность, представляющая роль пользователя.
 */
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Уникальный идентификатор роли.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя роли. Поле обязательно для заполнения и должно быть уникальным.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Набор пользователей, которым назначена данная роль.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
