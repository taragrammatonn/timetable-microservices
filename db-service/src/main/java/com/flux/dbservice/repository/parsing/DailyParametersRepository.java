package com.flux.dbservice.repository.parsing;

import com.flux.dbservice.entity.parsing.DailyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyParametersRepository extends JpaRepository<DailyParameters, Long> {
    DailyParameters getByParametersDate(String parametersDate);

    DailyParameters findByWeekNotNull();
}
