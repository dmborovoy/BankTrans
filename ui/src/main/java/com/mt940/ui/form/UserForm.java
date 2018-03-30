package com.mt940.ui.form;

import com.mt940.ui.form.groups.CreateGroup;
import com.mt940.ui.form.groups.UpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class UserForm {
    @NotNull(message = "userId is required", groups = {UpdateGroup.class})
    private Long userId;
    //@NotNull(message = "Username is required", groups = {CreateGroup.class})
    @NotEmpty(message = "Username is required", groups = {CreateGroup.class})
    @Size(message = "Wrong size of username", min = 3, max = 50, groups = {CreateGroup.class, UpdateGroup.class})
    private String username;
    @NotNull(message = "Password is required", groups = {CreateGroup.class})
    @Size(message = "Password size must be between {min} and {max}", min = 3, max = 50, groups = {CreateGroup.class, UpdateGroup.class})
    private String password;
    @NotNull(message = "Password confirmation is required", groups = {CreateGroup.class})
    @NotEmpty(message = "Password confirmation is required", groups = {CreateGroup.class})
    private String passwordAgain;
    @Size(message = "Description size must be between {min} and {max}", min = 3, max = 50, groups = {CreateGroup.class, UpdateGroup.class})
    private String description;
    @NotEmpty(message = "The role field cannot be empty", groups = {CreateGroup.class, UpdateGroup.class})
    private List<String> roles;
    @NotEmpty(message = "The instances field cannot be empty", groups = {CreateGroup.class, UpdateGroup.class})
    private List<String> instances;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @AssertTrue(message = "The password fields must match", groups = {CreateGroup.class, UpdateGroup.class})
    protected boolean isPasswordsMatch() {
        return ((password != null && password.equals(passwordAgain)) || (password == null && passwordAgain == null));
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getInstances() {
        return instances;
    }

    public void setInstances(List<String> instances) {
        this.instances = instances;
    }
}
