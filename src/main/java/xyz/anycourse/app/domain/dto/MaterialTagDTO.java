package xyz.anycourse.app.domain.dto;

import jakarta.validation.constraints.NotBlank;
import xyz.anycourse.app.domain.entity.Tag;

public class MaterialTagDTO {
    @NotBlank
    private String id;

    public MaterialTagDTO() {
    }

    public MaterialTagDTO(Tag tag) {
        this.id = tag.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
