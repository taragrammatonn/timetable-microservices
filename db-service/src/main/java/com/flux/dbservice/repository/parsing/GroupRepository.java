package com.flux.dbservice.repository.parsing;

import com.flux.dbservice.entity.parsing.lessons.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByName(String groupName);
}
