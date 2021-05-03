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
@Table(name = "group", schema = "lesson_parsing")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;
    Long groupId;
    String name;
}
