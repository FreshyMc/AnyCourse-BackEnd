package xyz.anycourse.app.domain.dto;

import xyz.anycourse.app.domain.entity.Tag;

public class TagDTO {
    private String id;
    private String name;

    public TagDTO() {
    }

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
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
}
