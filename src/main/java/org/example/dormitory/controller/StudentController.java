package org.example.dormitory.controller;

import org.example.dormitory.entity.Student;
import org.example.dormitory.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления информацией о студентах.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    /**
     * Конструктор для создания экземпляра контроллера.
     *
     * @param studentService сервис для работы со студентами.
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Получение всех студентов с возможностью фильтрации по ключевому слову и дате.
     *
     * @param keyword ключевое слово для фильтрации (необязательно).
     * @param date    дата для фильтрации (необязательно).
     * @return список студентов.
     */
    @GetMapping
    public ResponseEntity<List<Student>> listAllStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String date) {
        List<Student> students = studentService.listAll(keyword, date);
        return ResponseEntity.ok(students);
    }

    /**
     * Получение информации о студенте по его идентификатору.
     *
     * @param id идентификатор студента.
     * @return объект студента.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.get(id);
        return ResponseEntity.ok(student);
    }

    /**
     * Добавление нового студента.
     *
     * @param student объект студента для добавления.
     * @return объект добавленного студента.
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    /**
     * Обновление информации о студенте.
     *
     * @param id      идентификатор студента для обновления.
     * @param student объект студента с обновленными данными.
     * @return объект обновленного студента.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {
        student.setStudentId(id);
        studentService.update(student);
        return ResponseEntity.ok(student);
    }

    /**
     * Удаление студента по идентификатору.
     *
     * @param id идентификатор студента.
     * @return статус операции.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение количества активных резидентов (студентов, проживающих в общежитии).
     *
     * @return количество активных резидентов.
     */
    @GetMapping("/active-residents/count")
    public ResponseEntity<Integer> getActiveResidentsCount() {
        int count = studentService.countActiveResidents();
        return ResponseEntity.ok(count);
    }
}
