package org.example.dormitory.service;

import org.example.dormitory.entity.Role;
import org.example.dormitory.entity.User;
import org.example.dormitory.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса {@link UserDetailsService} для предоставления данных пользователя при аутентификации.
 *
 * Этот сервис получает данные пользователя из {@link UserRepository} и преобразует их
 * в {@link UserDetails}, которые требуются Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Создает новый экземпляр {@link CustomUserDetailsService} с указанным {@link UserRepository}.
     *
     * @param userRepository репозиторий для доступа к данным пользователя.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает данные пользователя по имени пользователя.
     *
     * @param username имя пользователя, идентифицирующее пользователя, данные которого необходимы.
     * @return объект {@link UserDetails}, содержащий данные пользователя.
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getName)
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
