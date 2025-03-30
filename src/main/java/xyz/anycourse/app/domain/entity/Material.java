package xyz.anycourse.app.domain.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "materials")
@Entity
public class Material extends Base {
    private String title;
    private String description;
    private String thumbnail;
    private String location;
    private String previewLocation;
    private String hlsPath;
    @ManyToMany
    @JoinTable(
        name = "material_tags",
        joinColumns = @JoinColumn(name = "material_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public Material() {
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

    public String getPreviewLocation() {
        return previewLocation;
    }

    public void setPreviewLocation(String previewLocation) {
        this.previewLocation = previewLocation;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getMaterials().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getMaterials().remove(this);
    }

    public String getHlsPath() {
        return hlsPath;
    }

    public void setHlsPath(String hlsPath) {
        this.hlsPath = hlsPath;
    }
}
