package org.example.dormitory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения для управления общежитием.
 * Является точкой входа для запуска Spring Boot приложения.
 */
@SpringBootApplication
public class DormitoryApplication {

    /**
     * Главный метод, используемый для запуска приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(DormitoryApplication.class, args);
    }

}
