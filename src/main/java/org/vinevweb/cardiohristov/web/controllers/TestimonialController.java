package org.vinevweb.cardiohristov.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.vinevweb.cardiohristov.domain.models.binding.TestimonialCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.TestimonialServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.AllProceduresProcedureViewModel;
import org.vinevweb.cardiohristov.domain.models.view.TestimonialViewModel;
import org.vinevweb.cardiohristov.errors.TestimonialCreateFailureException;
import org.vinevweb.cardiohristov.services.ProcedureService;
import org.vinevweb.cardiohristov.services.TestimonialService;
import org.vinevweb.cardiohristov.web.annotations.PageTitle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.vinevweb.cardiohristov.common.Constants.*;

@Controller
@RequestMapping("/testimonials")
public class TestimonialController extends BaseController {

    private final ModelMapper modelMapper;
    private final TestimonialService testimonialService;
    private final ProcedureService procedureService;


    @Autowired
    public TestimonialController(ModelMapper modelMapper, TestimonialService testimonialService, ProcedureService procedureService) {
        this.modelMapper = modelMapper;
        this.testimonialService = testimonialService;

        this.procedureService = procedureService;
    }

    @GetMapping("/all")
    @PageTitle(TITLE_TESTIMONIALS)
    public ModelAndView index(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        Map<String, Object> stringObjectMap = new HashMap<>();
        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());
        stringObjectMap.put("procedures", allProceduresProcedureViewModelSet);

        List<TestimonialViewModel> testimonialViewModels = testimonialService.findAllByOrderByWrittenOnDesc().stream()
                .map(p -> {
                    TestimonialViewModel testimonialViewModel = modelMapper.map(p, TestimonialViewModel.class);
                    testimonialViewModel.setUsername(p.getUser().getFirstName() + " " + p.getUser().getLastName());
                    return testimonialViewModel;
                })
                .collect(Collectors.toList());
        stringObjectMap.put("testimonials", testimonialViewModels);

        return super.view("testimonial-list", stringObjectMap);
    }


    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView createProcedureConfirm(@ModelAttribute TestimonialCreateBindingModel testimonialCreateBindingModel) {

        TestimonialServiceModel testimonialServiceModel = modelMapper.map(testimonialCreateBindingModel, TestimonialServiceModel.class);

        boolean result = testimonialService.createTestimonial(testimonialServiceModel);
        if (!result) {
            throw new TestimonialCreateFailureException(TESTIMONIAL_CREATION_ERROR);
        }

        return this.redirect("/testimonials/all");
    }



    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteProfile(@RequestParam("id") String id) {
        this.testimonialService
                .deleteTestimonial(id);
        return super.redirect("/testimonials/all");
    }


}
