package com.mt940.domain.permission;



import com.mt940.domain.converters.BkvRolesConverter;
import com.mt940.domain.enums.BKVRoles;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dimas on 25.11.2014.
 */

@Entity
@Table(schema = "bkv", name = "user")
public class BKVUser {

    private Long id;
    private String login;
    private String password;
    private String description;
    private boolean superAdmin;
    private boolean disabled;
    @Convert(converter = BkvRolesConverter.class)
    private Set<BKVRoles> roles;
    private Set<BKVInstance> instances;

    public BKVUser() {
    }

    public BKVUser(String login, String password, String description, boolean superAdmin) {
        this.login = login;
        this.password = password;
        this.description = description;
        this.superAdmin = superAdmin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "super_admin")
    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    @Column(name = "disabled")
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Convert(converter = BkvRolesConverter.class)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(schema = "bkv", name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    public Set<BKVRoles> getRoles() {
        return roles;
    }

    public void setRoles(BKVRoles... roles) {
        this.roles = new HashSet<BKVRoles>(Arrays.asList(roles));
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = "bkv", name = "user_instance", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "instance_id"))
    public Set<BKVInstance> getInstances() {
        return instances;
    }

    public void setInstances(Set<BKVInstance> instances) {
        this.instances = instances;
    }

    public void setRoles(Set<BKVRoles> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "BKVUser{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + "****" + '\'' +
                ", description='" + description + '\'' +
                ", superAdmin=" + superAdmin +
                ", disabled=" + disabled +
                //", roles=" + roles +
                //", instances=" + instances +
                "'}@" + Integer.toHexString(hashCode());
    }
}
