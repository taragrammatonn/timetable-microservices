package com.flux.dbservice.entity.parsing;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.text.SimpleDateFormat;
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

    @Basic(optional = false)
    @Column(name = "parameters_date" )
    private String parametersDate;


    @PrePersist
    protected void onCreate() {
        if (parametersDate == null) {
            parametersDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        }
    }
}
