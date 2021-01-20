package com.flux.dbservice.repository;

import com.flux.dbservice.entity.parsing.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson findByCoursName(String groupName);

    Lesson findByGroupName(String groupName);
}
