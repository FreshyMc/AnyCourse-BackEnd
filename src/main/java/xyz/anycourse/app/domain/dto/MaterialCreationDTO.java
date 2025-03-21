package xyz.anycourse.app.domain.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class MaterialCreationDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String shopId;
    private Set<MaterialTagDTO> tags = new HashSet<>();

    public MaterialCreationDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Set<MaterialTagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<MaterialTagDTO> tags) {
        this.tags = tags;
    }
}
