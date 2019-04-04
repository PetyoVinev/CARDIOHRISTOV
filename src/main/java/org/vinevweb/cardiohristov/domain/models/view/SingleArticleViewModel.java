package org.vinevweb.cardiohristov.domain.models.view;

import java.time.LocalDateTime;
import java.util.List;

public class SingleArticleViewModel {


    private String id;

    private String title;

    private String content;

    private LocalDateTime writtenOn;

    private String author;

    private List<SingleArticleCommentViewModel> comments;

    private String pictureUrl;

    public SingleArticleViewModel() {
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

    public List<SingleArticleCommentViewModel> getComments() {
        return comments;
    }

    public void setComments(List<SingleArticleCommentViewModel> comments) {
        this.comments = comments;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
