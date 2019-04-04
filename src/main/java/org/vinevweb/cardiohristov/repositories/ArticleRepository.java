package org.vinevweb.cardiohristov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vinevweb.cardiohristov.domain.entities.Article;

import java.util.List;


@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    List<Article> findAllByOrderByWrittenOnDesc();
    Article findByTitle(String title);

}
