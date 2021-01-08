package com.example.ppis.dto;

import com.example.ppis.model.Role;


public class UserDto {

    private Integer id;

    private String username;

    private String email;

    private Role role;

    private Boolean defaultPassword;

    public UserDto() {
    }

    public UserDto(Integer id, String username, String email, Role role, Boolean defaultPassword) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.defaultPassword = defaultPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(Boolean defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
}
