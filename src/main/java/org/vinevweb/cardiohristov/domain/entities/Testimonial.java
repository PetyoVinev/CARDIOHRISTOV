package org.vinevweb.cardiohristov.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="testimonials")
public class Testimonial extends BaseEntity {

    private LocalDateTime writtenOn;

    private User user;

    private String content;

    public Testimonial() {
    }

    @Column(name = "written_on", nullable = false)
    public LocalDateTime getWrittenOn() {
        return writtenOn;
    }

    public void setWrittenOn(LocalDateTime writtenOn) {
        this.writtenOn = writtenOn;
    }

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
