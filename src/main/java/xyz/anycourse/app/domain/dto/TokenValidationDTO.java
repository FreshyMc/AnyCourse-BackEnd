package xyz.anycourse.app.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenValidationDTO {
    @NotBlank
    private String token;

    public TokenValidationDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
