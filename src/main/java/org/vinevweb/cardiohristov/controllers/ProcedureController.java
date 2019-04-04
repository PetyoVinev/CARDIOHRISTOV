package org.vinevweb.cardiohristov.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.vinevweb.cardiohristov.domain.models.binding.ProcedureCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.ProfileDeleteBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.ProcedureServiceModel;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.AllProceduresProcedureViewModel;
import org.vinevweb.cardiohristov.errors.ProcedureCreateFailureException;
import org.vinevweb.cardiohristov.services.CloudinaryService;
import org.vinevweb.cardiohristov.services.ProcedureService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/procedures")
public class ProcedureController extends BaseController {
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ProcedureService procedureService;

    @Autowired
    public ProcedureController(ModelMapper modelMapper, CloudinaryService cloudinaryService, ProcedureService procedureService) {
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.procedureService = procedureService;
    }

    @GetMapping("/all")
    public ModelAndView getAll(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());

        return super.view("procedure-list", "procedures", allProceduresProcedureViewModelSet);
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createProcedure(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());

        return super.view("procedure-create", "procedures", allProceduresProcedureViewModelSet);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createProcedureConfirm(@ModelAttribute ProcedureCreateBindingModel procedureCreateBindingModel) throws IOException {
        ProcedureServiceModel procedureServiceModel = this.modelMapper
                .map(procedureCreateBindingModel, ProcedureServiceModel.class);


        String pictureUrl = this.cloudinaryService.uploadImage(procedureCreateBindingModel.getProcedurePicture());

        if (pictureUrl == null) {
            throw new ProcedureCreateFailureException("Procedure Picture upload failed.");
        }

        procedureServiceModel.setPictureUrl(pictureUrl);

        boolean result = this.procedureService
                .createProcedure(procedureServiceModel);

        if (!result) {
            throw new ProcedureCreateFailureException("Procedure creation failed.");
        }

        return this.redirect("/procedures/all");
    }

    @GetMapping("/detail/{name}")
    public ModelAndView getDetail(@PathVariable String name, @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel ){

        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());

        AllProceduresProcedureViewModel allProceduresProcedureViewModel =
                modelMapper.map(procedureService.findByName(name), AllProceduresProcedureViewModel.class);

        HashMap<String, Object> map = new HashMap<>(){{
            put("procedure", allProceduresProcedureViewModel);
            put("procedures", allProceduresProcedureViewModelSet);
        }};
        return super.view("procedure-detail", map);
    }


    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteProcedure(@RequestParam("id") String id) {
        this.procedureService
                .deleteProcedure(id);
        return super.redirect("/procedures/all");
    }

}
