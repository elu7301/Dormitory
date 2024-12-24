package org.example.dormitory.repository;

import org.example.dormitory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 *
 * Этот интерфейс предоставляет методы для взаимодействия с базой данных,
 * такие как поиск пользователя по имени.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по его имени пользователя (логину).
     *
     * @param username имя пользователя.
     * @return объект {@link Optional}, содержащий найденного пользователя, если он существует.
     */
    Optional<User> findByUsername(String username);
}
