package org.vinevweb.cardiohristov.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.vinevweb.cardiohristov.domain.models.binding.ProfileDeleteBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.UserRoleServiceModel;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.AllProceduresProcedureViewModel;
import org.vinevweb.cardiohristov.domain.models.view.AllUsersViewModel;
import org.vinevweb.cardiohristov.domain.models.view.LogViewModel;
import org.vinevweb.cardiohristov.domain.models.view.UserViewModel;
import org.vinevweb.cardiohristov.errors.UserEditFailureException;
import org.vinevweb.cardiohristov.services.LogService;
import org.vinevweb.cardiohristov.services.ProcedureService;
import org.vinevweb.cardiohristov.services.user.UserService;
import org.vinevweb.cardiohristov.web.annotations.PageTitle;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.vinevweb.cardiohristov.common.Constants.*;

@Controller
public class AdminController extends BaseController {



    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ProcedureService procedureService;
    private final LogService logService;


    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper, ProcedureService procedureService, LogService logService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.procedureService = procedureService;
        this.logService = logService;
    }

    @GetMapping("/profiles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(TITLE_USERS)
    public ModelAndView allProfiles(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        Map<String, Object> stringObjectMap = new HashMap<>();
        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());
        stringObjectMap.put("procedures", allProceduresProcedureViewModelSet);
        List<AllUsersViewModel> allUsersViewModels = this.userService.extractAllUsersOrderedAlphabetically()
                .stream()
                .map(u -> {
                    AllUsersViewModel allUsersViewModel = this.modelMapper.map(u, AllUsersViewModel.class);
                    allUsersViewModel.setFullName(String.format("%s %s", u.getFirstName(), u.getLastName()));

                    return allUsersViewModel;
                })
                .collect(Collectors.toList());
        stringObjectMap.put("profiles", allUsersViewModels);

        return super.view("all-profiles", stringObjectMap);
    }

    @GetMapping(value = "/api/profile{id}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public UserViewModel getProfile(@RequestParam("id") String id) {
        UserServiceModel userServiceModel = this.userService.extractUserById(id);
        UserViewModel userViewModel = this.modelMapper.map(userServiceModel, UserViewModel.class);
        userViewModel.setRoles(userServiceModel.getAuthorities().stream().map(UserRoleServiceModel::getAuthority).collect(Collectors.toList()));
        return userViewModel;
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editConfirm(@Valid @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult) {
        UserServiceModel userServiceModel = this.userService.extractUserByEmail(userRegisterBindingModel.getEmailRegister());

        if (!userRegisterBindingModel.getPasswordRegister().equals(userRegisterBindingModel.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userRegisterBindingModel", "passwordRegister",
                    PASSWORDS_DID_NOT_MATCH));
        }


        if (bindingResult.hasErrors()) {

                if ( (String)bindingResult.getRawFieldValue("passwordRegister") != "" ||
                        (String)bindingResult.getRawFieldValue("confirmPassword") != ""){
                        return super.view("all-profiles", "userRegisterBindingModel", userRegisterBindingModel);

                }

                if (bindingResult.getErrorCount() > 4 ){
                    return super.view("all-profiles", "userRegisterBindingModel", userRegisterBindingModel);
                }
        }


        if (!this.userService.editUser(this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class))) {
            throw new UserEditFailureException("Editing user " + userServiceModel.getEmail() + " failed.");
        }

        return super.redirect("/profiles");
    }

    @PostMapping("/profile/roleEdit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public String roleEditConfirm(@RequestBody String body, Principal principal) {
        String email = body.split("&")[0].split("=")[1].replace("%40", "@");
        String role = body.split("&")[1].split("=")[1];

        boolean result = this.userService.editUserRole(email, role);

        if (!result) {
            throw new UserEditFailureException("Editing user role" + email + " failed.");
        }

        return SUCCESS_MESSAGE;
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteProfile(@ModelAttribute ProfileDeleteBindingModel profileDeleteBindingModel) {
        UserServiceModel userServiceModel = this.modelMapper
                .map(profileDeleteBindingModel, UserServiceModel.class);

        this.userService
                .deleteProfile(userServiceModel);

        return super.redirect("/profiles");
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(TITLE_LOGS)
    public ModelAndView logs() {
        return super.view(
                "logs", "logViewModel",
                this.logService.getLogsOrderedByDate()
                        .stream()
                        .map(log -> this.modelMapper.map(log, LogViewModel.class))
                        .collect(Collectors.toList())
        );
    }



}
