package com.mt940.ui.domain;


import com.mt940.ui.domain.json.FieldName;

import java.util.Set;

public class UserDetails {
    @FieldName("Id")
    private Long id;
    @FieldName("Login")
    private String login;
    @FieldName(value = "Description", isEditable = true)
    private String description;
    @FieldName(value = "Super Admin", isEditable = true)
    private boolean superAdmin;
    @FieldName(value = "Disabled", isEditable = true)
    private boolean disabled;
    @FieldName(value = "Roles")
    private Set<String> roles;
    @FieldName(value = "Instances")
    private Set<String> instances;

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Set<String> getInstances() {
        return instances;
    }

    public void setInstances(Set<String> instances) {
        this.instances = instances;
    }
}
