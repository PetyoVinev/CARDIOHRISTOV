package org.vinevweb.cardiohristov.domain.models.view;

import java.time.LocalDateTime;

public class AllArticlesViewModel {


    private String id;

    private String title;

    private String content;

    private LocalDateTime writtenOn;

    private String author;

    private Integer commentsCount;

    private String pictureUrl;

    public AllArticlesViewModel() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getShortContent() {
        if (content.length() > 290){
            return content.substring(0, 290) + " ...";
        } else {
            return content;
        }

    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
