package org.vinevweb.cardiohristov.controllers;

import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public abstract class BaseController {

    public ModelAndView view(String viewName) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("fragments/base-layout");
        modelAndView.addObject("view", viewName);

        return modelAndView;
    }

    public ModelAndView view(String viewName, String objectName, Object object) {
        ModelAndView modelAndView = this.view(viewName);
        modelAndView.addObject(objectName, object);
        return modelAndView;
    }

    public ModelAndView view(String viewName, Map<String, Object> stringObjectMap) {
        ModelAndView modelAndView = this.view(viewName);

        for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
            modelAndView.addObject(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }

        return modelAndView;
    }

    public ModelAndView redirect(String url) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:" + url);

        return modelAndView;
    }
}
