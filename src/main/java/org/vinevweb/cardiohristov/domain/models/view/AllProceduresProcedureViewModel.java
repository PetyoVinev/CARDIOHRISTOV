package org.vinevweb.cardiohristov.domain.models.view;

import java.time.LocalDateTime;

public class AllProceduresProcedureViewModel {


    private String id;

    private String name;

    private String content;

    private String pictureUrl;

    public AllProceduresProcedureViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getShortContent() {
        if (content.length() > 140){
            return content.substring(0, 140) + " ...";
        } else {
            return content;
        }

    }
}
