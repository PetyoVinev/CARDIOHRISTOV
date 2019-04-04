package org.vinevweb.cardiohristov.domain.models.view;


import java.time.LocalDateTime;


public class SingleArticleCommentViewModel {

    private String content;

    private LocalDateTime writtenOn;

    private String username;

    public SingleArticleCommentViewModel() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
