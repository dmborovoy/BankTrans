package com.mt940.ui.controller;

import com.mt940.dao.jpa.BKVUserDao;
import com.mt940.domain.permission.BKVUser;
import com.mt940.ui.domain.UserDetails;
import com.mt940.ui.domain.json.ValidationErrorMessages;
import com.mt940.ui.form.UserForm;
import com.mt940.ui.form.groups.CreateGroup;
import com.mt940.ui.form.groups.UpdateGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@PreAuthorize("hasRole('UI_ADMIN')")
@Controller
@RequestMapping("/admin/api")
public class AdminApiController {
    //protected Logger l = LoggerFactory.getLogger(getClass());

    @Qualifier("bkvUserDaoImpl")
    @Autowired
    private BKVUserDao userDao;

    @Autowired
    private ConversionService conversionService;

    @Transactional
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody
    UserDetails addUser(@Validated({CreateGroup.class}) UserForm userForm) {
        BKVUser newUser = conversionService.convert(userForm, BKVUser.class);
        Assert.notNull(newUser);

        userDao.save(newUser);

        return conversionService.convert(newUser, UserDetails.class);
    }

    @Transactional
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public @ResponseBody
    UserDetails updateUser(@Validated({UpdateGroup.class}) UserForm userForm) {
        BKVUser toUpdate = userDao.findById(userForm.getUserId());
        BKVUser newEntry = conversionService.convert(userForm, BKVUser.class);
        Assert.notNull(newEntry);
        Assert.notNull(newEntry.getId());

        if (newEntry.getPassword() != null) {
            toUpdate.setPassword(newEntry.getPassword());
        }
        toUpdate.setDescription(newEntry.getDescription());
        toUpdate.setRoles(newEntry.getRoles());

        userDao.save(toUpdate);

        return conversionService.convert(toUpdate, UserDetails.class);
    }

    @Transactional
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    UserDetails get(@PathVariable Long id) {
        BKVUser user = userDao.findById(id);
        Assert.notNull(user, "user not found");

        return conversionService.convert(user, UserDetails.class);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ValidationErrorMessages validationExceptionHandler(Exception e) {
        ValidationErrorMessages message = new ValidationErrorMessages();

        if (e instanceof BindException) {
            BindException exception = (BindException) e;

            for (ObjectError error : exception.getGlobalErrors()) {
                message.addMessage(null, error.getDefaultMessage());
            }

            for (FieldError error : exception.getFieldErrors()) {
                message.addMessage(error.getField(), error.getDefaultMessage());
            }
        } else {
            log.error(e.getMessage(), e);
            message.addMessage(null, e.getMessage());
        }

        return message;
    }

}
