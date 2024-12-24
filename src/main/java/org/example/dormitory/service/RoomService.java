package org.example.dormitory.service;

import org.example.dormitory.entity.Room;
import org.example.dormitory.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Получение списка всех номеров, с возможностью поиска по ключевым словам.
     */
    public List<Room> listAll(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return roomRepository.search(keyword);
        }
        return roomRepository.findAll();
    }

    /**
     * Получение доступных номеров.
     */
    public List<Room> listAvailableRooms() {
        return roomRepository.findAvailableRooms();
    }

    /**
     * Получение номера комнаты по ID.
     */
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комната с ID " + id + " не найдена"));
    }

    /**
     * Сохранение или обновление номера.
     */
    public void save(Room room) {
        roomRepository.save(room);
    }

    /**
     * Удаление номера по ID.
     */
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комната с ID " + id + " не найдена"));
        roomRepository.delete(room);
    }

    /**
     * Получение номера комнаты по ID.
     * Если комната не найдена, выбрасывается исключение.
     */
    public Room get(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комната с ID " + id + " не найдена"));
    }


    /**
     * Удаление комнаты по ID без предварительного поиска.
     */
    public void deleteById(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("Комната с ID " + id + " не существует");
        }
        roomRepository.deleteById(id);
    }

    /**
     * Подсчет общего количества комнат.
     */
    public int countTotalRooms() {
        return (int) roomRepository.count();
    }

    public int countFreePlaces() {
        return roomRepository.findAvailableRooms()
                .stream()
                .mapToInt(Room::getFreePlaces)
                .sum();
    }

    public List<Map<String, Object>> getRoomConditionStatistics() {
        List<Object[]> rawData = roomRepository.countRoomsByCondition();
        List<Map<String, Object>> conditionStatistics = new ArrayList<>();

        for (Object[] entry : rawData) {
            if (entry.length >= 2 && entry[0] instanceof String && entry[1] instanceof Number) {
                Map<String, Object> map = new HashMap<>();
                map.put("condition", entry[0]); // Первое значение - состояние комнаты
                map.put("count", ((Number) entry[1]).intValue()); // Второе значение - количество
                conditionStatistics.add(map);
            } else {
                throw new IllegalArgumentException("Некорректный формат данных: " + Arrays.toString(entry));
            }
        }

        return conditionStatistics;
    }

}
