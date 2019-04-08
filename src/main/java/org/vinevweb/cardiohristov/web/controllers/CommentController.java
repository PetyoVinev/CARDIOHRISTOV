package org.vinevweb.cardiohristov.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;
import org.vinevweb.cardiohristov.domain.models.binding.CreateCommentBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.CommentServiceModel;
import org.vinevweb.cardiohristov.errors.CommentCreateFailureException;
import org.vinevweb.cardiohristov.services.ArticleService;
import org.vinevweb.cardiohristov.services.CommentService;

import java.io.UnsupportedEncodingException;


@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController {
    private final ModelMapper modelMapper;

    private final CommentService commentService;
    private final ArticleService articleService;

    @Autowired
    public CommentController(ModelMapper modelMapper, CommentService commentService, ArticleService articleService) {
        this.modelMapper = modelMapper;
        this.commentService = commentService;
        this.articleService = articleService;
    }


    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView createArticleConfirm(@ModelAttribute CreateCommentBindingModel createCommentBindingModel) throws UnsupportedEncodingException {
        CommentServiceModel commentServiceModel = this.modelMapper
                .map(createCommentBindingModel, CommentServiceModel.class);


        boolean result = commentService.createComment(commentServiceModel);
        if (!result) {
            throw new CommentCreateFailureException("Error occurred during comment creation.");
        }

        String url = "/articles/detail/" + articleService.findById(createCommentBindingModel.getArticleId()).getTitle();
       String path  =  UriUtils.encodePath(url, "UTF-8");
        return this.redirect(path);
    }


}
