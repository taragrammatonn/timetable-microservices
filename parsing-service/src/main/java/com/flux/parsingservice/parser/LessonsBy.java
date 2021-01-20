package com.flux.parsingservice.parser;


public enum LessonsBy {

    GROUP(1, "http://orar.usarb.md/api/getlessons"),
    TEACHER(2, "http://orar.usarb.md/api/getlessonsByTeacher"),
    AUDIENCES(3, "http://orar.usarb.md/api/getlessonsByOffice"),
    DEFAULT(0, "http://orar.usarb.md/api/getlessons");

    private final int apiId;
    private final String api;

    LessonsBy(int i, String s) {
        this.apiId = i;
        this.api = s;
    }

    public int getApiId() {
        return apiId;
    }

    public String getApi() {
        return api;
    }
}
