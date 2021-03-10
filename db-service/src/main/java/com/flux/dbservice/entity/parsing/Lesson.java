package com.flux.dbservice.entity.parsing;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "lesson", schema = "lesson_parsing")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;
    String coursName;
    String lessonNumber;
    Integer dayNumber;
    String coursOffice;
    String teacherName;
    String groupName;
    String subGroupName;
}
