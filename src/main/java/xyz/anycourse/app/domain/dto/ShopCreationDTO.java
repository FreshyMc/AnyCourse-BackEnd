package xyz.anycourse.app.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class ShopCreationDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    public ShopCreationDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
