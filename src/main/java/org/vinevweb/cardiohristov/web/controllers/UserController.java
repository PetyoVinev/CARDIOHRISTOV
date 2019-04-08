package org.vinevweb.cardiohristov.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.errors.UserRegisterFailureException;
import org.vinevweb.cardiohristov.services.user.UserService;
import javax.validation.Valid;

import static org.vinevweb.cardiohristov.common.Constants.PASSWORDS_DID_NOT_MATCH;
import static org.vinevweb.cardiohristov.common.Constants.USER_REGISTRATION_FAILURE;

@Controller
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel,
                                        BindingResult bindingResult) {

        if (!userRegisterBindingModel.getPasswordRegister().equals(userRegisterBindingModel.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userRegisterBindingModel", "passwordRegister",
                    PASSWORDS_DID_NOT_MATCH));
        }

        if (bindingResult.hasErrors()) {
            return super.view("index", "userRegisterBindingModel", userRegisterBindingModel);
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);

        if (!this.userService.registerUser(userServiceModel)) {
            throw new UserRegisterFailureException(String.format(USER_REGISTRATION_FAILURE, userServiceModel.getEmail()));
        }


        return super.view("index", "userRegisterBindingModel", userRegisterBindingModel);
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return super.redirect("/");
    }


}
