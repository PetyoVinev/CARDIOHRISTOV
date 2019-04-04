package org.vinevweb.cardiohristov.domain.models.binding;

import org.springframework.web.multipart.MultipartFile;

public class ArticleCreateBindingModel {


    private String title;

    private String content;

    private MultipartFile articlePicture;


    public ArticleCreateBindingModel() {
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

    public MultipartFile getArticlePicture() {
        return articlePicture;
    }

    public void setArticlePicture(MultipartFile articlePicture) {
        this.articlePicture = articlePicture;
    }
}
