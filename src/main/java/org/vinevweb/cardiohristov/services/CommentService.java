package org.vinevweb.cardiohristov.services;


import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.models.service.CommentServiceModel;

public interface CommentService {
    boolean createComment(CommentServiceModel commentServiceModel);

    void removeCommentFromUserAndDelete(Comment comment);

    void removeCommentFromArticleAndDelete(Comment comment);
}
