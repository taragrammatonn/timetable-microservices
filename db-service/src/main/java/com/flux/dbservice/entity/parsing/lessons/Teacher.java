package com.flux.dbservice.entity.parsing.lessons;

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
@Table(name = "teacher", schema = "lesson_parsing")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;
    String name;
}
