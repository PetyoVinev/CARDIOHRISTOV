package org.vinevweb.cardiohristov.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.vinevweb.cardiohristov.web.annotations.PageTitle;

@Controller
public class AccessController extends BaseController {
    @PageTitle("unauthorized")
    @GetMapping("/unauthorized")
    public ModelAndView unauthorized() {
        return this.view("error/unauthorized");
    }
}
