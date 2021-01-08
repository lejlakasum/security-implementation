package com.example.ppis.dto;

import com.example.ppis.model.Role;

import javax.persistence.ManyToMany;
import javax.validation.constraints.Pattern;
import java.util.List;

public class UserRegisterDTO {

    private String username;

    private String email;

    private UserRoleDTO role;

    public UserRegisterDTO() {}

    public UserRegisterDTO(String username, String email, UserRoleDTO role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRoleDTO getRole() {
        return role;
    }

    public void setRole(UserRoleDTO role) {
        this.role = role;
    }
}
