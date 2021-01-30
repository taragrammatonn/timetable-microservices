package com.flux.dbservice.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "user_option", schema = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Boolean groupSelected;
    Boolean teacherSelected;
    Boolean audienceSelected;
    Long userId;

    @Transient
    Long chatId;
}
