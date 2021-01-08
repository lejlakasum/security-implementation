package com.example.ppis.dto;

public class ChangePasswordDto {

    private String oldPassword;

    private String newPassword;

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
