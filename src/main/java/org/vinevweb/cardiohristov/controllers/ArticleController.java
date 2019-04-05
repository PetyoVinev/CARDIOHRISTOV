package org.vinevweb.cardiohristov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.models.binding.ArticleCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.ArticleServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.AllArticlesViewModel;
import org.vinevweb.cardiohristov.domain.models.view.AllProceduresProcedureViewModel;
import org.vinevweb.cardiohristov.domain.models.view.SingleArticleCommentViewModel;
import org.vinevweb.cardiohristov.domain.models.view.SingleArticleViewModel;
import org.vinevweb.cardiohristov.errors.ArticleCreateFailureException;
import org.vinevweb.cardiohristov.services.ArticleService;
import org.vinevweb.cardiohristov.services.CloudinaryService;
import org.vinevweb.cardiohristov.services.ProcedureService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;

@Controller
@RequestMapping("/articles")
public class ArticleController extends BaseController {
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ArticleService articleService;
    private final ProcedureService procedureService;

    @Autowired
    public ArticleController(ModelMapper modelMapper, CloudinaryService cloudinaryService,
                             ArticleService articleService, ProcedureService procedureService) {
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.articleService = articleService;
        this.procedureService = procedureService;
    }

    @GetMapping("/all")
    public ModelAndView index(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());
        stringObjectMap.put("procedures", allProceduresProcedureViewModelSet);

        List<AllArticlesViewModel> allArticlesViewModels = articleService.findAllByOrderByWrittenOnDesc().stream()
                .map(p -> {
                    AllArticlesViewModel allArticlesViewModel = modelMapper.map(p, AllArticlesViewModel.class);
                    allArticlesViewModel.setAuthor(p.getAuthor().getFirstName() + " " + p.getAuthor().getLastName());
                    allArticlesViewModel.setCommentsCount(p.getComments().size());
                    return allArticlesViewModel;
                })
                .collect(Collectors.toList());
        stringObjectMap.put("articles", allArticlesViewModels);

        return super.view("article-list", stringObjectMap);
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createArticle(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());

        return super.view("article-create","procedures", allProceduresProcedureViewModelSet);
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createArticleConfirm(@ModelAttribute ArticleCreateBindingModel articleCreateBindingModel) throws IOException {
        ArticleServiceModel articleServiceModel = this.modelMapper
                .map(articleCreateBindingModel, ArticleServiceModel.class);


        /*String pictureUrl = this.cloudinaryService.uploadImage(articleCreateBindingModel.getArticlePicture());*/
        String pictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq6jS89uE7QegpxXMefUXPFEY04QFVAir55Fu7RN3rBe81YbFuCQ";
        if (pictureUrl == null) {
            throw new ArticleCreateFailureException("Article Picture upload failed.");
        }

        articleServiceModel.setPictureUrl(pictureUrl);

        boolean result = this.articleService
                .createArticle(articleServiceModel);

        if (!result) {
            throw new ArticleCreateFailureException("Article creation failed.");
        }

        return this.redirect("/articles/all");
    }

    @GetMapping("/detail/{title}")
    public ModelAndView getDetail(@PathVariable String title , @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel){

        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());

        ArticleServiceModel articleServiceModel = articleService.findByTitle(title);

        SingleArticleViewModel singleArticleViewModel =
                modelMapper.map(articleServiceModel, SingleArticleViewModel.class);
        singleArticleViewModel.setComments(getViewComments(articleServiceModel));

        List<AllArticlesViewModel> allArticlesViewModels = articleService.findAllByOrderByWrittenOnDesc().stream()
                .map(p -> {
                    AllArticlesViewModel allArticlesViewModel = modelMapper.map(p, AllArticlesViewModel.class);
                    allArticlesViewModel.setAuthor(p.getAuthor().getFirstName() + " " + p.getAuthor().getLastName());
                    allArticlesViewModel.setCommentsCount(p.getComments().size());
                    return allArticlesViewModel;
                })
                .collect(Collectors.toList());

        HashMap<String, Object> map = new HashMap<>(){{
            put("article", singleArticleViewModel);
            put("procedures", allProceduresProcedureViewModelSet);
            put("articles", allArticlesViewModels);
        }};
        return super.view("article-detail", map);
    }


    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteArticle(@RequestParam("id") String id) {
        this.articleService
                .deleteArticle(id);
        return super.redirect("/articles/all");
    }

    private List<SingleArticleCommentViewModel> getViewComments(ArticleServiceModel articleServiceModel) {
        List<SingleArticleCommentViewModel> singleArticleCommentViewModels = new ArrayList<>();
        for (Comment comment : articleServiceModel.getComments()) {
            SingleArticleCommentViewModel singleArticleCommentViewModel = new SingleArticleCommentViewModel();
            singleArticleCommentViewModel.setContent(comment.getContent());
            singleArticleCommentViewModel.setUsername(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
            singleArticleCommentViewModel.setWrittenOn(comment.getWrittenOn());
            singleArticleCommentViewModels.add(singleArticleCommentViewModel);

        }
        return singleArticleCommentViewModels.stream().sorted(Comparator.comparing(SingleArticleCommentViewModel::getWrittenOn, reverseOrder())).collect(Collectors.toUnmodifiableList());
    }


}
