package org.vinevweb.cardiohristov.domain.entities;


import org.vinevweb.cardiohristov.config.LocalDateTimeAttributeConverter;

import javax.persistence.Column;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "procedures")
public class Procedure extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    private String content;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "image_url", nullable = true)
    private String pictureUrl;

    public Procedure() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
