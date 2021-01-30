package com.flux.telegramservice.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserVO {

    public UserVO(Long chatId, String fName, String lName, String userNickName, String userGroup, String userLanguage, Boolean active, Boolean adminEntity, Boolean isDefined) {
        this.chatId = chatId;
        this.fName = fName;
        this.lName = lName;
        this.userNickName = userNickName;
        this.userGroup = userGroup;
        this.userLanguage = userLanguage;
        this.active = active;
        this.adminEntity = adminEntity;
        this.isDefined = isDefined;
    }

    public UserVO(Long chatId, String fName, String lName) {
        this.chatId = chatId;
        this.fName = fName;
        this.lName = lName;
    }

    public UserVO(Long chatId, UserOptionVO userOption) {
        this.chatId = chatId;
        this.userOption = userOption;
    }

    Long chatId;
    String fName;
    String lName;
    String userNickName;
    String userGroup;
    String userLanguage;
    Boolean active;
    Boolean adminEntity;
    Boolean isDefined;

    UserOptionVO userOption;
}
