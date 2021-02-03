package com.flux.dbservice.repository.parsing;

import com.flux.dbservice.entity.parsing.lessons.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
