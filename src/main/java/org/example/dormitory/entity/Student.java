package org.example.dormitory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * Сущность, представляющая студента.
 */
@Entity
public class Student {

    /**
     * Уникальный идентификатор студента.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    /**
     * Имя студента. Поле обязательно для заполнения.
     */
    @NotBlank(message = "Вы пропустили это поле")
    private String firstName;

    /**
     * Фамилия студента. Поле обязательно для заполнения.
     */
    @NotBlank(message = "Вы пропустили это поле")
    private String lastName;

    /**
     * Email студента. Поле обязательно для заполнения и должно соответствовать формату email.
     */
    @Email(message = "Неверный формат email")
    @NotBlank(message = "Вы пропустили это поле")
    private String email;

    /**
     * Номер телефона студента. Поле обязательно для заполнения и должно соответствовать формату.
     */
    @Pattern(regexp = "^(\\+[0-9]{11}|8[0-9]{10})$", message = "Неправильно заполнен номер")
    @NotBlank(message = "Вы пропустили это поле")
    private String phoneNumber;

    /**
     * Дата заселения студента. Поле обязательно для заполнения.
     */
    @NotNull(message = "Вы пропустили это поле")
    private LocalDate checkinDate;

    /**
     * Дата выселения студента. Поле обязательно для заполнения.
     */
    @NotNull(message = "Вы пропустили это поле")
    private LocalDate checkoutDate;

    /**
     * Комната, в которой проживает студент. Связь с сущностью {@link Room}.
     */
    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
