package org.vinevweb.cardiohristov.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.vinevweb.cardiohristov.domain.entities.Article;
import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.ArticleServiceModel;
import org.vinevweb.cardiohristov.repositories.ArticleRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl implements ArticleService {


    private final ArticleRepository articleRepository;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    private final CommentService commentService;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper, UserRepository userRepository, CommentService commentService) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }


    @Override
    public boolean createArticle(ArticleServiceModel articleServiceModel) {

          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        Article article = this.modelMapper.map(articleServiceModel, Article.class);
        article.setAuthor(userRepository.findById(currentUser.getId()).orElse(null));
        article.setWrittenOn(LocalDateTime.now());


        this.articleRepository.save(article);

        return true;
    }

    @Override
    public List<ArticleServiceModel> findAllByOrderByWrittenOnDesc() {
        return this.articleRepository
                .findAllByOrderByWrittenOnDesc()
                .stream()
                .map(x -> this.modelMapper.map(x, ArticleServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ArticleServiceModel findById(String id) {
        return modelMapper.map(Objects.requireNonNull(this.articleRepository.findById(id).orElse(null)), ArticleServiceModel.class);
    }


    @Override
    public ArticleServiceModel findByTitle(String title) {
        return modelMapper.map(Objects.requireNonNull(this.articleRepository.findByTitle(title)), ArticleServiceModel.class);
    }

    @Override
    public void deleteArticle(String id) {
        Article article  = articleRepository.findById(id).orElse(null);

        Set<Comment> comments = new HashSet<>(article.getComments());

        for (Comment comment : comments) {
            article.getComments().remove(comment);
            articleRepository.saveAndFlush(article);
            commentService.removeCommentFromUserAndDelete(comment);
        }


        this.articleRepository.deleteById(id);
    }
}
