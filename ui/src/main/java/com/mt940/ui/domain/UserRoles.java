package com.mt940.ui.domain;


public enum UserRoles {
    UI_ADMIN("UI_ADMIN"), UI_USER("UI_USER"), UI_GUEST("UI_GUEST");
    private String name;

    UserRoles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
