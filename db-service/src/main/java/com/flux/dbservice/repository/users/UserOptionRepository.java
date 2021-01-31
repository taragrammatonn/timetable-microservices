package com.flux.dbservice.repository.users;

import com.flux.dbservice.entity.users.UserOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOptionRepository extends JpaRepository<UserOption, Long> {
    UserOption findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
