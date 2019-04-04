package org.vinevweb.cardiohristov.domain.models.binding;


public class CreateCommentBindingModel {

    private String content;


    private String articleId;

    public CreateCommentBindingModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
