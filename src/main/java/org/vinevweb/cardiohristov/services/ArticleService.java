package org.vinevweb.cardiohristov.services;


import org.vinevweb.cardiohristov.domain.models.service.ArticleServiceModel;

import java.util.List;

public interface ArticleService {
    boolean createArticle(ArticleServiceModel articleServiceModel);

    List<ArticleServiceModel> findAllByOrderByWrittenOnDesc();

    ArticleServiceModel findById(String id);

    ArticleServiceModel findByTitle(String title);

    void deleteArticle(String id);
}
