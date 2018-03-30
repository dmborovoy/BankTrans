package com.mt940.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {
    @RequestMapping("/")
    public String index() {
        return "redirect:/emails";
    }
}
