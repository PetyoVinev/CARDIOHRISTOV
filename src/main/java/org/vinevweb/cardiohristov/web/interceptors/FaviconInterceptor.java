package org.vinevweb.cardiohristov.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FaviconInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String faviconPath = "https://res.cloudinary.com/cardiohristov/image/upload/v1549366668/favicon.png";

        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        }

        modelAndView.addObject("favicon", faviconPath);
    }
}
