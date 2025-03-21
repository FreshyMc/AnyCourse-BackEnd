package xyz.anycourse.app.domain.dto;

import xyz.anycourse.app.domain.entity.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialCreatedDTO {
    private String id;
    private String title;
    private String description;
    private String shopId;
    private Set<MaterialTagDTO> tags = new HashSet<>();

    public MaterialCreatedDTO() {
    }

    public MaterialCreatedDTO(Material material) {
        this.id = material.getId();
        this.title = material.getTitle();
        this.description = material.getDescription();
        this.shopId = material.getShop().getId();
        this.tags = material.getTags().stream()
                .map(MaterialTagDTO::new)
                .collect(Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
