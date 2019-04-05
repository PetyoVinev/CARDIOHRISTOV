package org.vinevweb.cardiohristov.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Log extends BaseEntity {

    private LocalDateTime dateTime;
    private String user;
    private String event;

    public Log() {
    }

    @Column(name = "date_time", nullable = false, updatable = false)
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "user")
    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Column(name = "event", columnDefinition = "text")
    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
