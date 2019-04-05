package org.vinevweb.cardiohristov.domain.models.service;

import java.time.LocalDateTime;

public class LogServiceModel {

    private LocalDateTime dateTime;
    private String user;
    private String event;

    public LogServiceModel() {
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
