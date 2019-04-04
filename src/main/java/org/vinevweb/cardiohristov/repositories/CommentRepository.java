package org.vinevweb.cardiohristov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vinevweb.cardiohristov.domain.entities.Comment;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    void deleteAllByArticleId(String articleId);

}
