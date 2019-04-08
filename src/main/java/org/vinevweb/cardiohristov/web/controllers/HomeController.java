package org.vinevweb.cardiohristov.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.view.AllArticlesViewModel;
import org.vinevweb.cardiohristov.domain.models.view.AllProceduresProcedureViewModel;
import org.vinevweb.cardiohristov.domain.models.view.TestimonialViewModel;
import org.vinevweb.cardiohristov.services.ArticleService;
import org.vinevweb.cardiohristov.services.ProcedureService;
import org.vinevweb.cardiohristov.services.TestimonialService;
import org.vinevweb.cardiohristov.web.annotations.PageTitle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {
    private final ModelMapper modelMapper;
    private final ProcedureService procedureService;
    private final TestimonialService testimonialService;
    private final ArticleService articleService;

    @Autowired
    public HomeController(ModelMapper modelMapper, ProcedureService procedureService, TestimonialService testimonialService, ArticleService articleService) {
        this.modelMapper = modelMapper;
        this.procedureService = procedureService;
        this.testimonialService = testimonialService;
        this.articleService = articleService;
    }

    @GetMapping("/")
    public ModelAndView index(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        Map<String, Object> stringObjectMap = new HashMap<>();

        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());

        List<TestimonialViewModel> testimonialViewModels = testimonialService.findAllByOrderByWrittenOnAsc().stream()
                .map(p -> {
                   TestimonialViewModel testimonialViewModel = modelMapper.map(p, TestimonialViewModel.class);
                    testimonialViewModel.setUsername(p.getUser().getFirstName() + " " + p.getUser().getLastName());
                   return testimonialViewModel;
                })
                .limit(2)
                .collect(Collectors.toList());

        List<AllArticlesViewModel> allArticlesViewModels = articleService.findAllByOrderByWrittenOnDesc().stream()
                .map(p -> {
                    AllArticlesViewModel allArticlesViewModel = modelMapper.map(p, AllArticlesViewModel.class);
                    allArticlesViewModel.setCommentsCount(p.getComments().size());
                    return allArticlesViewModel;
                })
                .limit(3)
                .collect(Collectors.toList());



        stringObjectMap.put("procedures", allProceduresProcedureViewModelSet);
        stringObjectMap.put("testimonials", testimonialViewModels);
        stringObjectMap.put("articles", allArticlesViewModels);

        return super.view("index", stringObjectMap);
    }

    @GetMapping("/contactUs")
    @PageTitle("Контакти")
    public ModelAndView contactUs(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        Map<String, Object> stringObjectMap = new HashMap<>();
        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());
        stringObjectMap.put("procedures", allProceduresProcedureViewModelSet);

        return super.view("contactUs", stringObjectMap);
    }
}
