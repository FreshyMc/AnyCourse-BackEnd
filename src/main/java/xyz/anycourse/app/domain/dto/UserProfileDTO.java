package xyz.anycourse.app.domain.dto;

import xyz.anycourse.app.domain.entity.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProfileDTO {
    private String id;
    private String username;
    private String avatar;
    private Set<ShopDTO> following = new HashSet<>();
    private Date joinedAt;

    public UserProfileDTO() {
    }

    public UserProfileDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
        //TODO optimize this if followed shops gets bigger in size
        this.following = user.getShops().stream()
                .map(ShopDTO::new)
                .collect(Collectors.toSet());
        this.joinedAt = user.getCreatedAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<ShopDTO> getFollowing() {
        return following;
    }

    public void setFollowing(Set<ShopDTO> following) {
        this.following = following;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }
}
