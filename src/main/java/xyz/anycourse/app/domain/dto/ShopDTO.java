package xyz.anycourse.app.domain.dto;

import org.springframework.security.core.context.SecurityContextHolder;
import xyz.anycourse.app.domain.UserPrincipal;
import xyz.anycourse.app.domain.entity.Shop;
import xyz.anycourse.app.util.UserUtil;

public class ShopDTO {
    private String id;
    private String name;
    private String description;
    private String thumbnail;
    private Integer followersCount;
    private String ownerId;
    private String ownerUsername;
    private String ownerAvatar;
    private Boolean following;

    public ShopDTO() {
    }

    public ShopDTO(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.description = shop.getDescription();
        this.thumbnail = shop.getThumbnail();
        this.followersCount = shop.getFollowers().size();
        this.ownerId = shop.getOwner().getId();
        this.ownerUsername = shop.getOwner().getUsername();
        this.ownerAvatar = shop.getOwner().getAvatar();
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
        this.following = shop.getFollowers().stream().anyMatch(follower -> follower.getId().equals(principal.getUserId()));
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

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerAvatar() {
        return ownerAvatar;
    }

    public void setOwnerAvatar(String ownerAvatar) {
        this.ownerAvatar = ownerAvatar;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }
}
