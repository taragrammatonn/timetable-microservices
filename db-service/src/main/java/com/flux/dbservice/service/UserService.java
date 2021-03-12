package com.flux.dbservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.dbservice.entity.users.User;
import com.flux.dbservice.entity.users.UserOption;
import com.flux.dbservice.repository.users.UserOptionRepository;
import com.flux.dbservice.repository.users.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserOptionRepository userOptionRepository;
    private final ObjectMapper objectMapper;

    public UserService(UserRepository userRepository, UserOptionRepository userOptionRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.userOptionRepository = userOptionRepository;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public String saveUser(String userJson) {
        User jsonUser = objectMapper.readValue(userJson, User.class);
        User dbUser = userRepository.findByChatId(jsonUser.getChatId());

        if (!isNull(dbUser)) {
            jsonUser.setId(dbUser.getId());
        }
        return objectMapper.writeValueAsString(userRepository.save(jsonUser));
    }

    @SneakyThrows
    public String getUserByChatId(String chatId) {
        return objectMapper.writeValueAsString(userRepository.findByChatId(Long.valueOf(chatId)));
    }

    @SneakyThrows
    @Transactional
    public void saveUserOptions(String userOptionJson) {
        UserOption jsonUserOption = objectMapper.readValue(userOptionJson, UserOption.class);
        User user = userRepository.findByChatId(jsonUserOption.getChatId());
        UserOption userOption = userOptionRepository.findByUserId(user.getId());

        if (!isNull(userOption)) {
            jsonUserOption.setId(userOption.getId());
        }

        jsonUserOption.setUserId(user.getId());
        user.setUserOption(jsonUserOption);

        userRepository.save(user);
    }

    @SneakyThrows
    public String getUserOptionByChatId(Long chatId) {
        return objectMapper.writeValueAsString(userOptionRepository.findByUserId(
                userRepository.findByChatId(chatId).getId()));
    }
}
