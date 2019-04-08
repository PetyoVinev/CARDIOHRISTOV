package org.vinevweb.cardiohristov.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.vinevweb.cardiohristov.web.annotations.PageTitle;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TitleInterceptor extends HandlerInterceptorAdapter {


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String title = "Д-р ХРИСТОВ";

        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        } else {
            if (handler instanceof HandlerMethod) {
                PageTitle methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(PageTitle.class);

                if (methodAnnotation != null) {
                    modelAndView
                            .addObject("title", title + " - " + methodAnnotation.value());
                } else {
                    modelAndView
                            .addObject("title", title);
                }
            }
        }
    }
}
