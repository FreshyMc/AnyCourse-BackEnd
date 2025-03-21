package xyz.anycourse.app.domain.dto;

import xyz.anycourse.app.domain.entity.Material;

public class MaterialDTO {
    private String id;
    private String title;
    private String description;
    private String thumbnail;
    private String location;

    public MaterialDTO(Material material) {
        this.id = material.getId();
        this.title = material.getTitle();
        this.description = material.getDescription();
        this.thumbnail = material.getThumbnail();
        this.location = material.getLocation();
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
