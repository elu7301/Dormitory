package org.example.dormitory.controller;

import org.example.dormitory.entity.Student;
import org.example.dormitory.entity.Room;
import org.example.dormitory.service.StudentService;
import org.example.dormitory.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Контроллер, отвечающий за отображение страниц и обработку запросов
 * по работе со студентами и комнатами общежития.
 */
@Controller
public class AppController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private RoomService roomService;

    /**
     * Отображает главную страницу.
     *
     * @return имя представления главной страницы
     */
    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    /**
     * Отображает страницу со списком студентов.
     *
     * @param model      объект Model для передачи данных в представление
     * @param keyword    ключевое слово для поиска
     * @param date       дата для фильтрации
     * @param roomNumber номер комнаты для фильтрации
     * @param sortOrder  порядок сортировки (asc/desc)
     * @return имя представления со списком студентов
     */
    @GetMapping("/students")
    public String viewStudentsPage(Model model,
                                   @Param("keyword") String keyword,
                                   @Param("date") String date,
                                   @Param("roomNumber") String roomNumber,
                                   @Param("sortOrder") String sortOrder) {
        List<Student> listStudents = studentService.listAll(keyword, date);

        if (sortOrder != null) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                listStudents.sort(Comparator.comparing(Student::getCheckinDate));
            } else if ("desc".equalsIgnoreCase(sortOrder)) {
                listStudents.sort(Comparator.comparing(Student::getCheckinDate).reversed());
            }
        }

        model.addAttribute("listStudents", listStudents);
        model.addAttribute("keyword", keyword);
        model.addAttribute("date", date);
        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("sortOrder", sortOrder);

        return "students";
    }

    /**
     * Отображает страницу со списком комнат.
     *
     * @param model     объект Model для передачи данных в представление
     * @param keyword   ключевое слово для поиска
     * @param sortOrder порядок сортировки (asc/desc)
     * @return имя представления со списком комнат
     */
    @GetMapping("/rooms")
    public String viewRoomsPage(Model model, @Param("keyword") String keyword, @Param("sortOrder") String sortOrder) {
        List<Room> listRooms = roomService.listAll(keyword);

        if (sortOrder != null) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                listRooms.sort(Comparator.comparingInt(Room::getFreePlaces));
            } else if ("desc".equalsIgnoreCase(sortOrder)) {
                listRooms.sort(Comparator.comparingInt(Room::getFreePlaces).reversed());
            }
        }

        model.addAttribute("listRooms", listRooms);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortOrder", sortOrder);

        return "rooms";
    }

    /**
     * Отображает форму для добавления нового студента.
     *
     * @param model объект Model для передачи данных в представление
     * @return имя представления формы добавления студента
     */
    @GetMapping("/add_student")
    @PreAuthorize("hasRole('ADMIN')")
    public String showNewStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("rooms", roomService.listAvailableRooms());
        return "add_student";
    }

    /**
     * Сохраняет нового студента в базе данных.
     *
     * @param student       объект студента для сохранения
     * @param bindingResult результаты валидации полей
     * @param model         объект Model для передачи данных в представление
     * @return перенаправление на список студентов или повторное открытие формы при ошибках
     */
    @PostMapping("/save_student")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rooms", roomService.listAvailableRooms());
            return "add_student";
        }

        try {
            studentService.save(student);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", roomService.listAvailableRooms());
            return "add_student";
        }

        return "redirect:/students";
    }

    /**
     * Отображает форму для редактирования данных существующего студента.
     *
     * @param id идентификатор студента
     * @return объект ModelAndView с формой редактирования и данными студента
     */
    @GetMapping("/edit_student/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showEditStudentForm(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("edit_student");
        Student student = studentService.get(id);

        if (student == null) {
            throw new IllegalArgumentException("Студент с ID " + id + " не найден");
        }

        mav.addObject("student", student);
        mav.addObject("rooms", roomService.listAvailableRooms());
        return mav;
    }

    /**
     * Обновляет данные студента в базе данных.
     *
     * @param student       объект студента для обновления
     * @param bindingResult результаты валидации полей
     * @param model         объект Model для передачи данных в представление
     * @return перенаправление на список студентов или повторное открытие формы при ошибках
     */
    @PostMapping("/update_student")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStudent(@Valid @ModelAttribute("student") Student student,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rooms", roomService.listAvailableRooms());
            return "edit_student";
        }

        try {
            studentService.update(student);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", roomService.listAvailableRooms());
            return "edit_student";
        }

        return "redirect:/students";
    }

    /**
     * Удаляет студента по его идентификатору.
     *
     * @param id идентификатор студента
     * @return перенаправление на список студентов
     */
    @GetMapping("/delete_student/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/students";
    }

    /**
     * Отображает форму для добавления новой комнаты.
     *
     * @param model объект Model для передачи данных в представление
     * @return имя представления формы добавления комнаты
     */
    @GetMapping("/add_room")
    @PreAuthorize("hasRole('ADMIN')")
    public String showNewRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "add_room";
    }

    /**
     * Сохраняет новую комнату в базе данных.
     *
     * @param room          объект комнаты для сохранения
     * @param bindingResult результаты валидации полей
     * @return перенаправление на список комнат или повторное открытие формы при ошибках
     */
    @PostMapping("/save_room")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveRoom(@Valid @ModelAttribute("room") Room room,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add_room";
        }
        roomService.save(room);
        return "redirect:/rooms";
    }

    /**
     * Отображает форму для редактирования данных комнаты.
     *
     * @param id идентификатор комнаты
     * @return объект ModelAndView с формой редактирования и данными комнаты
     */
    @GetMapping("/edit_room/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showEditRoomForm(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("edit_room");
        Room room = roomService.get(id);

        if (room == null) {
            throw new IllegalArgumentException("Комната с ID " + id + " не найдена");
        }

        mav.addObject("room", room);
        return mav;
    }

    /**
     * Обновляет данные комнаты в базе данных.
     *
     * @param room          объект комнаты для обновления
     * @param bindingResult результаты валидации полей
     * @return перенаправление на список комнат или повторное открытие формы при ошибках
     */
    @PostMapping("/update_room")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateRoom(@Valid @ModelAttribute("room") Room room,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit_room";
        }
        roomService.save(room);
        return "redirect:/rooms";
    }

    /**
     * Удаляет комнату по её идентификатору.
     *
     * @param id идентификатор комнаты
     * @return перенаправление на список комнат
     */
    @GetMapping("/delete_room/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
        return "redirect:/rooms";
    }

    /**
     * Отображает страницу "О разработчике".
     *
     * @return имя представления страницы о разработчике
     */
    @GetMapping("/about_author")
    public String viewAboutAuthorPage() {
        return "about_author";
    }

    /**
     * Отображает страницу со статистикой.
     *
     * @param model объект Model для передачи данных в представление
     * @return имя представления страницы со статистикой
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewStatisticsPage(Model model) {
        model.addAttribute("totalResidents", studentService.countActiveResidents());
        model.addAttribute("totalRooms", roomService.countTotalRooms());
        model.addAttribute("freePlaces", roomService.countFreePlaces());
        return "statistics";
    }

    /**
     * Возвращает данные для гистограммы заселения студентов.
     *
     * @return список с данными в формате JSON
     */
    @GetMapping("/histogram-arrival")
    @ResponseBody
    public List<Map<String, Object>> getHistogramArrivalData() {
        return studentService.getStudentArrivalHistogram();
    }

    /**
     * Возвращает статистику по состоянию комнат.
     *
     * @return список с данными в формате JSON
     */
    @GetMapping("/room-condition-data")
    @ResponseBody
    public List<Map<String, Object>> getRoomConditionStatistics() {
        return roomService.getRoomConditionStatistics();
    }
}
