package com.flux.telegramservice.util;

/**
 * Class with only util links.
 */
public class Links {

    private Links() {}

    // LOGISTIC-SERVICE API's
    public static final String LOGISTIC_SERVICE = "http://LOGISTIC-SERVICE/logistic-api";
    public static final String SAVE_USER = "/addUser";
    public static final String GET_USER_BY_CHAT_ID = "/getUser?chatId={chatId}";
    public static final String FIND_GROUP = "/findGroup?groupName={groupName}";
    public static final String SAVE_HISTORY = "/saveHistory";
    public static final String SAVE_USER_OPTION = "/saveUserOption";
    public static final String GET_USER_OPTION_BY_CHAT_ID = "/getUserOption?chatId={chatId}";
    public static final String GET_LESSONS = "/getLessons?groupJson={groupJson}&day={day}";
    public static final String GET_LESSONS_WITH_PARAM = "/getLessons?groupJson={groupJson}&day={day}";

    // Util
    public static final String NULL = "null";
}
