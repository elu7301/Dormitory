package org.example.dormitory.controller;

import org.example.dormitory.entity.Room;
import org.example.dormitory.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления информацией о комнатах в общежитии.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    /**
     * Конструктор для создания экземпляра контроллера.
     *
     * @param roomService сервис для работы с комнатами.
     */
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Получение всех комнат с возможностью фильтрации по ключевому слову.
     *
     * @param keyword ключевое слово для фильтрации (необязательно).
     * @return список комнат.
     */
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms(@RequestParam(required = false) String keyword) {
        List<Room> rooms = roomService.listAll(keyword);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Получение информации о конкретной комнате по её идентификатору.
     *
     * @param id идентификатор комнаты.
     * @return объект комнаты.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomService.get(id);
        return ResponseEntity.ok(room);
    }

    /**
     * Добавление новой комнаты.
     *
     * @param room объект комнаты для добавления.
     * @return объект добавленной комнаты.
     */
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        roomService.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    /**
     * Обновление информации о комнате.
     *
     * @param id   идентификатор комнаты для обновления.
     * @param room объект комнаты с обновленными данными.
     * @return объект обновленной комнаты.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        room.setRoomId(id);
        roomService.save(room);
        return ResponseEntity.ok(room);
    }

    /**
     * Удаление комнаты по её идентификатору.
     *
     * @param id идентификатор комнаты.
     * @return статус операции.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение статистики по состоянию комнат.
     *
     * @return список статистических данных по состоянию комнат.
     */
    @GetMapping("/condition-statistics")
    public ResponseEntity<List<Map<String, Object>>> getRoomConditionStatistics() {
        List<Map<String, Object>> statistics = roomService.getRoomConditionStatistics();
        return ResponseEntity.ok(statistics);
    }
}
