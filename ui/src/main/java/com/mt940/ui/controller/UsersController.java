package com.mt940.ui.controller;

import com.mt940.dao.jpa.BKVUserDao;
import com.mt940.domain.enums.BKVRoles;
import com.mt940.domain.enums.Instance;
import com.mt940.domain.permission.BKVUser;
import com.mt940.ui.domain.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('UI_ADMIN')")
@Controller
@RequestMapping("/admin/users")
public class UsersController {
    @Qualifier("bkvUserDaoImpl")
    @Autowired
    private BKVUserDao userDao;

    @Autowired
    private ConversionService conversionService;

    @Transactional
    @RequestMapping
    public String users(Model model) {
        model.addAttribute("items", convert(userDao.findAll()));
        model.addAttribute("roles", BKVRoles.values());
        model.addAttribute("instances", Instance.values());
        return "users";
    }


    @Transactional
    @RequestMapping("/remove")
    public String remove(@NotNull @RequestParam("id") Long userId) {
        userDao.deleteById(userId);
        userDao.flush();

        return "redirect:/admin/users";
    }

    @Transactional
    @RequestMapping("/disable")
    public String disable(@NotNull @RequestParam("id") Long userId) {
        return updateActiveStatus(userId, true);
    }

    @Transactional
    @RequestMapping("/enable")
    public String enable(@NotNull @RequestParam("id") Long userId) {
        return updateActiveStatus(userId, false);
    }

    private String updateActiveStatus(Long userId, boolean isDisabled) {
        BKVUser user = userDao.findById(userId);
        Assert.notNull(user, "user not found");

        user.setDisabled(isDisabled);
        userDao.save(user);

        return "redirect:/admin/users";
    }

    private List<UserDetails> convert(List<BKVUser> source) {
        return source
                .stream()
                .map(s -> conversionService.convert(s, UserDetails.class))
                .collect(Collectors.toList());
    }
}
