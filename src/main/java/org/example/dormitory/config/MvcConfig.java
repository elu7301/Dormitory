package org.example.dormitory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация MVC для определения простых маршрутов и представлений.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Регистрация контроллеров представлений для маршрутов.
     *
     * @param registry объект для регистрации маршрутов и их представлений
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/students").setViewName("students");
        registry.addViewController("/rooms").setViewName("rooms");
        registry.addViewController("/add_student").setViewName("add_student");
        registry.addViewController("/edit_student").setViewName("edit_student");
        registry.addViewController("/add_room").setViewName("add_room");
        registry.addViewController("/edit_room").setViewName("edit_room");
        registry.addViewController("/statistics").setViewName("statistics");
        registry.addViewController("/about_author").setViewName("about_author");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
    }
}
