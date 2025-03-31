package xyz.anycourse.app.domain.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "shops")
@Entity
public class Shop extends Base {
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String thumbnail;
    @ManyToOne
    private User owner;
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Material> products = new ArrayList<>();
    @ManyToMany
    @JoinTable(
        name = "shop_follower",
        joinColumns = @JoinColumn(name = "shop_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> followers = new HashSet<>();

    public Shop() {
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Material> getProducts() {
        return products;
    }

    public void setProducts(List<Material> products) {
        this.products = products;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public void addFollower(User user) {
        this.followers.add(user);
        user.getShops().add(this);
    }

    public void removeFollower(User user) {
        this.followers.remove(user);
        user.getShops().remove(this);
    }
}
