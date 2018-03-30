package com.mt940.ui.controller;

import com.mt940.dao.repository.BkvInstanceRepository;
import com.mt940.domain.enums.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasRole('UI_ADMIN')")
@Controller
@RequestMapping("/admin/system")
public class SystemController {
    @Autowired
    private BkvInstanceRepository instanceRepository;

    @RequestMapping
    public String system(Model model) {
        model.addAttribute("items", Instance.values());
        return "system";
    }
}
