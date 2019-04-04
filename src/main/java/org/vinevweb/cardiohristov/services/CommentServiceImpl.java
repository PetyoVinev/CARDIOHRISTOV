package org.vinevweb.cardiohristov.services;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.CommentServiceModel;
import org.vinevweb.cardiohristov.repositories.ArticleRepository;
import org.vinevweb.cardiohristov.repositories.CommentRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {


    private final ArticleRepository articleRepository;

    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(ArticleRepository articleRepository, CommentRepository commentRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }


    @Override
    public boolean createComment(CommentServiceModel commentServiceModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();
        Comment comment = this.modelMapper.map(commentServiceModel, Comment.class);
        comment.setUser(userRepository.findById(currentUser.getId()).orElse(null));
        comment.setArticle(articleRepository.findById(commentServiceModel.getArticleId()).orElse(null));
        comment.setWrittenOn(LocalDateTime.now());

        this.commentRepository.save(comment);

        return true;
    }


    @Override
    public void deleteComment(Comment comment) {
        User user = this.userRepository.findById(comment.getUser().getId()).orElse(null);
        user.getComments().remove(comment);
        userRepository.saveAndFlush(user);
        commentRepository.delete(comment);

    }



}
