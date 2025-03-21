package xyz.anycourse.app.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class UserProfileUpdateDTO {
    @NotBlank
    private String username;

    public UserProfileUpdateDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
