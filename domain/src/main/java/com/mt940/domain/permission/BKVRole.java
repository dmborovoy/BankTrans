package com.mt940.domain.permission;


import com.mt940.domain.enums.BKVRoles;

import javax.persistence.*;

@Entity
@Table(schema = "bkv", name = "dict_role")
public class BKVRole {

    private Integer id;
    private String description;
    private BKVRoles role;

    public BKVRole() {
    }

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public BKVRoles getRole() {
        return BKVRoles.findByCode(id);
    }

    @Transient
    public void setRole(BKVRoles role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "BKVRole{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", role=" + getRole() +
                "'}@" + Integer.toHexString(hashCode());
    }
}
