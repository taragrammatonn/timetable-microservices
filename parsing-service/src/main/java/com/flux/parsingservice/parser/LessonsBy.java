package com.flux.parsingservice.parser;


import java.util.EnumSet;

public enum LessonsBy {

    /* used in stream */
    AUDIENCES("audienceSelected", "http://orar.usarb.md/api/getlessonsByOffice"),
    GROUP("groupSelected", "http://orar.usarb.md/api/getlessons"),
    TEACHER("teacherSelected", "http://orar.usarb.md/api/getlessonsByTeacher");

    private final String apiId;
    private final String api;

    LessonsBy(String i, String s) {
        this.apiId = i;
        this.api = s;
    }

    public String getApiId() {
        return apiId;
    }

    public static LessonsBy findByValue(String val) {
        return EnumSet.allOf(LessonsBy.class)
                .stream()
                .filter(e -> e.getApiId().equals(val))
                .findFirst()
                .orElse(null);
    }

    public String getApi() {
        return api;
    }
}
