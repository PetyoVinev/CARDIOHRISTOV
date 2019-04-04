package org.vinevweb.cardiohristov.domain.models.service;

import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.entities.User;

import java.time.LocalDateTime;
import java.util.Set;

public class ArticleServiceModel {


    private String id;

    private String title;

    private String content;

    private LocalDateTime writtenOn;

    private User author;

    private Set<Comment> comments;

    private String pictureUrl;

    public ArticleServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getWrittenOn() {
        return writtenOn;
    }

    public void setWrittenOn(LocalDateTime writtenOn) {
        this.writtenOn = writtenOn;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
