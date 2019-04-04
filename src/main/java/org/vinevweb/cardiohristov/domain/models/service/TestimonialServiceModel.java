package org.vinevweb.cardiohristov.domain.models.service;

import org.vinevweb.cardiohristov.domain.entities.BaseEntity;
import org.vinevweb.cardiohristov.domain.entities.User;

import java.time.LocalDateTime;


public class TestimonialServiceModel {

    private String id;

    private LocalDateTime writtenOn;

    private User user;

    private String content;

    public TestimonialServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getWrittenOn() {
        return writtenOn;
    }

    public void setWrittenOn(LocalDateTime writtenOn) {
        this.writtenOn = writtenOn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
