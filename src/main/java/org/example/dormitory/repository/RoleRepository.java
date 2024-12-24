package org.example.dormitory.repository;

import org.example.dormitory.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 *
 * Этот интерфейс предоставляет методы для взаимодействия с базой данных,
 * такие как поиск роли по имени.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Ищет роль по её имени.
     *
     * @param name имя роли.
     * @return объект {@link Optional}, содержащий найденную роль, если она существует.
     */
    Optional<Role> findByName(String name);
}
