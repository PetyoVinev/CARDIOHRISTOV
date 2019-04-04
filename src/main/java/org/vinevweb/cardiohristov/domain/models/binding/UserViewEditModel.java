package org.vinevweb.cardiohristov.domain.models.binding;


import org.vinevweb.cardiohristov.domain.entities.UserRole;

import java.util.Set;

public class UserViewEditModel {

    private String id;

    private String userName;

    private Set<UserRole> authorities;

    public UserViewEditModel(String id, String userName, Set<UserRole> authorities) {
        this.id = id;
        this.userName = userName;
        this.authorities = authorities;
    }

    public UserViewEditModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<UserRole> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }
}

