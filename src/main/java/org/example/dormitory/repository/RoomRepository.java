package org.example.dormitory.repository;

import org.example.dormitory.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Поиск номеров по ключевым словам.
     * Можно искать по номеру комнаты, состоянию, этажу, доступности.
     */
    @Query("SELECT r FROM Room r WHERE " +
            "LOWER(CONCAT(r.roomNumber, ' ', r.roomCondition, ' ', r.floor, ' ', r.totalPlaces, ' ', r.available)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Room> search(@Param("keyword") String keyword);

    /**
     * Поиск доступных номеров.
     */
    @Query("SELECT r FROM Room r WHERE r.available = true")
    List<Room> findAvailableRooms();

    /**
     * Поиск номеров по состоянию.
     */
    @Query("SELECT r FROM Room r WHERE r.roomCondition = :condition")
    List<Room> findByCondition(@Param("condition") String condition);

    /**
     * Поиск номеров по количеству свободных мест.
     */
    @Query("SELECT r FROM Room r WHERE r.freePlaces >= :minFreePlaces")
    List<Room> findRoomsByFreePlaces(@Param("minFreePlaces") int minFreePlaces);

    /**
     * Поиск номеров по этажу.
     */
    @Query("SELECT r FROM Room r WHERE r.floor = :floor")
    List<Room> findByFloor(@Param("floor") int floor);

    /**
     * Сортировка номеров по номеру комнаты.
     */
    @Query("SELECT r FROM Room r ORDER BY r.roomNumber ASC")
    List<Room> sortByRoomNumberAsc();

    @Query("SELECT r FROM Room r ORDER BY r.roomNumber DESC")
    List<Room> sortByRoomNumberDesc();

    /**
     * Поиск комнаты по ID с использованием Optional.
     */
    Optional<Room> findById(Long id);

    /**
     * Подсчет количества комнат по состоянию.
     */
    @Query("SELECT r.roomCondition, COUNT(r) FROM Room r GROUP BY r.roomCondition")
    List<Object[]> countRoomsByCondition();
}
