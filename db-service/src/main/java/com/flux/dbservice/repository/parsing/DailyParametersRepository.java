package com.flux.dbservice.repository.parsing;

import com.flux.dbservice.entity.parsing.lessons.DailyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyParametersRepository extends JpaRepository<DailyParameters, Long> {
    DailyParameters getByParametersDate(LocalDate parametersDate);

    DailyParameters findByWeekNotNull();
}
