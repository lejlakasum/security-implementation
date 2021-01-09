package com.example.ppis.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRoleDTO {

    @NotBlank
    @NotNull
    private Integer roleId;

    private String name;

    public UserRoleDTO() {}

    public UserRoleDTO(@NotBlank Integer roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
