package xyz.anycourse.app.domain.dto;

import xyz.anycourse.app.domain.entity.Material;

import java.util.Set;
import java.util.stream.Collectors;

public class MaterialDTO {
    private String id;
    private String title;
    private String description;
    private Set<TagDTO> tags;
    private ShopDTO shop;

    public MaterialDTO(Material material) {
        this.id = material.getId();
        this.title = material.getTitle();
        this.description = material.getDescription();
        this.tags = material.getTags().stream()
                .map(TagDTO::new)
                .collect(Collectors.toSet());
        this.shop = new ShopDTO(material.getShop());
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

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public ShopDTO getShop() {
        return shop;
    }

    public void setShop(ShopDTO shop) {
        this.shop = shop;
    }
}
