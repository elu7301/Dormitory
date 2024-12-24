package org.example.dormitory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Сущность, представляющая комнату в общежитии.
 */
@Entity
public class Room {

    /**
     * Уникальный идентификатор комнаты.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    /**
     * Номер комнаты. Поле обязательно для заполнения.
     */
    @NotNull(message = "Номер комнаты обязателен")
    private int roomNumber;

    /**
     * Общее количество мест в комнате. Поле обязательно для заполнения.
     */
    @NotNull(message = "Общее количество мест обязательно")
    private int totalPlaces;

    /**
     * Количество свободных мест в комнате. Поле обязательно для заполнения.
     */
    @NotNull(message = "Количество свободных мест обязательно")
    private int freePlaces;

    /**
     * Состояние комнаты (например, "Отремонтирована", "Требует ремонта"). Поле обязательно для заполнения.
     */
    @NotBlank(message = "Состояние комнаты обязательно")
    private String roomCondition;

    /**
     * Этаж, на котором находится комната. Поле обязательно для заполнения.
     */
    @NotNull(message = "Поле этаж обязательно")
    private int floor;

    /**
     * Статус доступности комнаты (может ли быть использована для заселения). Поле обязательно для заполнения.
     */
    @NotNull(message = "Свободна ли комната обязательно")
    private boolean available;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getTotalPlaces() {
        return totalPlaces;
    }

    public void setTotalPlaces(int totalPlaces) {
        this.totalPlaces = totalPlaces;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    public String getRoomCondition() {
        return roomCondition;
    }

    public void setRoomCondition(String roomCondition) {
        this.roomCondition = roomCondition;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber=" + roomNumber +
                ", totalPlaces=" + totalPlaces +
                ", freePlaces=" + freePlaces +
                ", roomCondition='" + roomCondition + '\'' +
                ", floor=" + floor +
                ", available=" + available +
                '}';
    }
}
