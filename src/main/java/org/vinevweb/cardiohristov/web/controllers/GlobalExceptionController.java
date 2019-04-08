package org.vinevweb.cardiohristov.web.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import static org.vinevweb.cardiohristov.common.Constants.SOMETHING_WENT_WRONG;

@ControllerAdvice
public class GlobalExceptionController extends BaseController {



    @ExceptionHandler(RuntimeException.class)
    public ModelAndView getException(RuntimeException re) {

        if (re.getClass().getSimpleName().equals("AccessDeniedException")){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("/error/unauthorized");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("fragments/base-layout");
            modelAndView.addObject("view", "/error/error-template");
            modelAndView.addObject("message", re.getClass().isAnnotationPresent(ResponseStatus.class)
                    ? re.getClass().getAnnotation(ResponseStatus.class).reason()
                    : SOMETHING_WENT_WRONG);

            return modelAndView;
        }


    }
}
