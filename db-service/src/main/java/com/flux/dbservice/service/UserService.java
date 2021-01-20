package com.flux.dbservice.service;

import com.flux.dbservice.entity.user.User;
import com.flux.dbservice.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Gson gson;

    public String saveUser(String userJson) {
        return gson.toJson(userRepository.save(gson.fromJson(userJson, User.class)));
    }
}
