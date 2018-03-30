package com.mt940.ui.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainMenuInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null
                && handler instanceof HandlerMethod
                && modelAndView.hasView()
                && !modelAndView.getViewName().startsWith("redirect:")) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            modelAndView.addObject("controllerName", handlerMethod
                    .getBeanType().getSimpleName());
            modelAndView.addObject("controllerAction", handlerMethod.getMethod()
                    .getName());
        }
    }
}
