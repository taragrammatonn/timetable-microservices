package com.flux.dbservice.entity.parsing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "daily_parameters", schema = "parsing")
public class DailyParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    String day;
    String semester;
    String week;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate parametersDate;
}
