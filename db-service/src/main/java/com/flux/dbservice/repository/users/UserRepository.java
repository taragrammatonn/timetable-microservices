package com.flux.dbservice.repository.users;

import com.flux.dbservice.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

    Boolean existsByChatId(Long chatId);
}
