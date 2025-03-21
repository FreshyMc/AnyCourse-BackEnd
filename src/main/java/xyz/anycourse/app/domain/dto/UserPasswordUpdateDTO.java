package xyz.anycourse.app.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class UserPasswordUpdateDTO {
    @NotBlank
    private String password;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String confirmNewPassword;

    public UserPasswordUpdateDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
