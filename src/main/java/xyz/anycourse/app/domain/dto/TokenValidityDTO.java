package xyz.anycourse.app.domain.dto;

public class TokenValidityDTO {
    private Boolean expired;

    public TokenValidityDTO() {
    }

    public TokenValidityDTO(Boolean expired) {
        this.expired = expired;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
}
