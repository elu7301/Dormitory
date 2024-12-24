package org.example.dormitory.service;

import org.example.dormitory.entity.Room;
import org.example.dormitory.entity.Student;
import org.example.dormitory.repository.RoomRepository;
import org.example.dormitory.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис для управления студентами.
 * Предоставляет методы для добавления, обновления, удаления и анализа данных о студентах.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepo;
    private final RoomRepository roomRepo;

    /**
     * Конструктор для внедрения зависимостей {@link StudentRepository} и {@link RoomRepository}.
     *
     * @param studentRepo репозиторий для работы с сущностями {@link Student}
     * @param roomRepo    репозиторий для работы с сущностями {@link Room}
     */
    public StudentService(StudentRepository studentRepo, RoomRepository roomRepo) {
        this.studentRepo = studentRepo;
        this.roomRepo = roomRepo;
    }

    /**
     * Получение всех студентов с возможностью поиска по ключевому слову и дате.
     *
     * @param keyword ключевое слово для поиска (опционально)
     * @param date    дата заселения (опционально)
     * @return список студентов, соответствующих критериям поиска
     */
    public List<Student> listAll(String keyword, String date) {
        if (keyword != null && !keyword.isEmpty() && date != null && !date.isEmpty()) {
            return studentRepo.search(keyword, date);
        }
        return studentRepo.findAll();
    }

    /**
     * Сохранение нового студента с автоматическим обновлением информации о комнате.
     *
     * @param student объект студента для сохранения
     * @throws IllegalStateException если в комнате нет свободных мест
     * @throws IllegalArgumentException если указанная комната не найдена
     */
    public void save(Student student) {
        Room room = roomRepo.findById(student.getRoom().getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Комната с ID " + student.getRoom().getRoomId() + " не найдена"));

        if (room.getFreePlaces() <= 0) {
            throw new IllegalStateException("Нет свободных мест в комнате номер " + room.getRoomNumber());
        }

        room.setFreePlaces(room.getFreePlaces() - 1);
        room.setAvailable(room.getFreePlaces() > 0);
        roomRepo.save(room);

        studentRepo.save(student);
    }

    /**
     * Обновление информации о студенте, включая обработку смены комнаты.
     *
     * @param student объект студента с обновленными данными
     * @throws IllegalArgumentException если студент или указанная комната не найдены
     */
    public void update(Student student) {
        Room room = roomRepo.findById(student.getRoom().getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Комната с ID " + student.getRoom().getRoomId() + " не найдена"));

        Student existingStudent = studentRepo.findById(student.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Студент с ID " + student.getStudentId() + " не найден"));

        if (!existingStudent.getRoom().getRoomId().equals(room.getRoomId())) {
            Room oldRoom = existingStudent.getRoom();
            oldRoom.setFreePlaces(oldRoom.getFreePlaces() + 1);
            oldRoom.setAvailable(true);
            roomRepo.save(oldRoom);

            room.setFreePlaces(room.getFreePlaces() - 1);
            room.setAvailable(room.getFreePlaces() > 0);
            roomRepo.save(room);
        }

        studentRepo.save(student);
    }

    /**
     * Получение информации о студенте по его идентификатору.
     *
     * @param id идентификатор студента
     * @return объект {@link Student}
     * @throws IllegalArgumentException если студент с указанным ID не найден
     */
    public Student get(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Студент с ID " + id + " не найден"));
    }

    /**
     * Удаление студента с обновлением информации о комнате.
     *
     * @param id идентификатор студента
     * @throws IllegalArgumentException если студент с указанным ID не найден
     */
    public void delete(Long id) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Студент с ID " + id + " не найден"));

        Room room = student.getRoom();
        if (room != null) {
            room.setFreePlaces(room.getFreePlaces() + 1);
            room.setAvailable(true);
            roomRepo.save(room);
        }

        studentRepo.delete(student);
    }

    /**
     * Подсчет количества активных резидентов (студентов, проживающих в общежитии).
     *
     * @return количество активных резидентов
     */
    public int countActiveResidents() {
        return studentRepo.countActiveResidents();
    }

    /**
     * Получение гистограммы заселения студентов.
     *
     * @return список карт с датой заселения и количеством студентов на каждую дату
     * @throws IllegalArgumentException если данные имеют некорректный формат
     */
    public List<Map<String, Object>> getStudentArrivalHistogram() {
        List<Object[]> histogramData = studentRepo.countStudentsByArrivalDate();
        List<Map<String, Object>> histogram = new ArrayList<>();

        for (Object[] data : histogramData) {
            if (data.length >= 2 && data[0] != null && data[1] instanceof Number) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("date", data[0].toString());
                map.put("count", ((Number) data[1]).intValue());
                histogram.add(map);
            } else {
                throw new IllegalArgumentException("Некорректный формат данных: " + Arrays.toString(data));
            }
        }

        return histogram;
    }
}
