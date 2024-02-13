package com.example.micrometerboot.mysql.repository;

import com.example.micrometerboot.mysql.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.id = :id")
    Student findStudentById(Long id);

    Student findByName(String name);

}
