package org.example.dormitory.repository;

import org.example.dormitory.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Поиск студентов по ключевым словам и дате заселения.
     */
    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(CONCAT(s.firstName, ' ', s.lastName, ' ', s.email, ' ', s.phoneNumber, ' ', s.room.roomNumber)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "AND s.checkinDate = :date")
    List<Student> search(@Param("keyword") String keyword, @Param("date") String date);

    /**
     * Подсчет активных резидентов (тех, кто еще проживает в общежитии).
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.checkoutDate > CURRENT_DATE")
    int countActiveResidents();

    /**
     * Подсчет количества студентов по датам заселения.
     */
    @Query("SELECT s.checkinDate, COUNT(s) FROM Student s GROUP BY s.checkinDate ORDER BY s.checkinDate ASC")
    List<Object[]> countStudentsByArrivalDate();
}
