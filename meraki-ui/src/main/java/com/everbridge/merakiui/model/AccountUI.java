package com.everbridge.merakiui.model;

/**
 * Created by kangliu on 10/13/16.
 */
public class AccountUI {
    private String id;
    private String email;
    private String password;
    private boolean admin;
    private String organization;

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganization() {
        return organization;
    }
}
