package com.mt940.domain.permission;

import com.mt940.domain.enums.Instance;

import javax.persistence.*;

@Entity
@Table(schema = "bkv", name = "dict_instance")
public class BKVInstance {

    private Integer id;
    private String code;
    private String value;
    private boolean allowed;
    private Instance instance;

    public BKVInstance() {
    }

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "allowed")
    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    @Transient
    public Instance getInstance() {
        return Instance.findByCode(id);
    }

    @Transient
    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "BKVInstance{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", allowed=" + allowed +
                ", instance=" + getInstance() +
                "'}@" + Integer.toHexString(hashCode());
    }
}
