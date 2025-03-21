package xyz.anycourse.app.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class ShopModificationDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    public ShopModificationDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
