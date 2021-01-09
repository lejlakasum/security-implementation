package com.example.ppis.dto;

import javax.validation.constraints.*;

public class ChangePasswordDto {

    @NotNull
    @NotEmpty
    @NotBlank
    private String oldPassword;

    @NotEmpty
    @NotBlank
    @NotNull
    @Pattern(regexp = "^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!{}<>@^*()_=;:'\\-#$%&? \"]).*$")
    private String newPassword;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(([^<>()\\\\.,;:\\s@\"]+(\\.[^<>()\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")
    private String email;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String oldPassword, String newPassword, String email) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
