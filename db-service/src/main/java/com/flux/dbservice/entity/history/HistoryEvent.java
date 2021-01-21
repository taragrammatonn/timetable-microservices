package com.flux.dbservice.entity.history;

public enum HistoryEvent {
    GROUP ("GROUP"),
    TEACHER ("TEACHER"),
    AUDIENCE ("AUDIENCE"),
    NEW_USER ("NEW_USER");

    private final String event;

    HistoryEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
