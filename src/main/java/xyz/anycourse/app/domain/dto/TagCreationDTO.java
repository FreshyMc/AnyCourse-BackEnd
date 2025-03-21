package xyz.anycourse.app.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class TagCreationDTO {
    @NotBlank
    public String name;

    public TagCreationDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
