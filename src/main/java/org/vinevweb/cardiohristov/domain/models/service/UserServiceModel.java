package org.vinevweb.cardiohristov.domain.models.service;


import java.util.List;

public class UserServiceModel {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordRegister;
    private List<UserRoleServiceModel> authorities;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordRegister() {
        return passwordRegister;
    }

    public void setPasswordRegister(String passwordRegister) {
        this.passwordRegister = passwordRegister;
    }

    public List<UserRoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserRoleServiceModel> authorities) {
        this.authorities = authorities;
    }
}
