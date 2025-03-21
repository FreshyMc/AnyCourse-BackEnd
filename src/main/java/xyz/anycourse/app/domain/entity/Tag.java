package xyz.anycourse.app.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Table(name = "tags")
@Entity
public class Tag extends Base {
    private String name;
    @ManyToMany(mappedBy = "tags")
    private Set<Material> materials;

    public Tag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }
}
