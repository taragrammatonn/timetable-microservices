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
public class UserOptionVO {

    Long chatId;
    Boolean groupSelected;
    Boolean teacherSelected;
    Boolean audienceSelected;

    public UserOptionVO groupSelected(Long chatId) {
        return new UserOptionVO(chatId, true, false, false);
    }

    public UserOptionVO teacherSelected(Long chatId) {
        return new UserOptionVO(chatId, false, true, false);
    }

    public UserOptionVO audienceSelected(Long chatId) {
        return new UserOptionVO(chatId, false, false, true);
    }
}