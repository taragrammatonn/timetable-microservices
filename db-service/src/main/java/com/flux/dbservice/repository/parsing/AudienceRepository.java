package com.flux.dbservice.repository.parsing;

import com.flux.dbservice.entity.parsing.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long> {

}
