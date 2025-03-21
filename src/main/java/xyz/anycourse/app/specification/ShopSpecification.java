package xyz.anycourse.app.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import xyz.anycourse.app.domain.entity.Shop;

public class ShopSpecification {

    public static Specification<Shop> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Shop> hasOwner(String ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Shop> hasNameAndOwner(String name, String ownerId) {
        return Specification.where(hasName(name)).and(hasOwner(ownerId));
    }

    public static Predicate nameContains(String name, Root<Shop> root, CriteriaBuilder criteriaBuilder) {
        if (name == null || name.isEmpty()) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Predicate hasOwnerId(String ownerId, Root<Shop> root, CriteriaBuilder criteriaBuilder) {
        if (ownerId == null || ownerId.isEmpty()) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Predicate isFollower(String followerId, Root<Shop> root, CriteriaBuilder criteriaBuilder) {
        if (followerId == null || followerId.isEmpty()) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.equal(root.get("followers").get("id"), followerId);
    }

    public static Specification<Shop> filterBy(String name, String ownerId, String followerId) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            predicate = criteriaBuilder.and(predicate, nameContains(name, root, criteriaBuilder));

            predicate = criteriaBuilder.and(predicate, hasOwnerId(ownerId, root, criteriaBuilder));

            predicate = criteriaBuilder.and(predicate, isFollower(followerId, root, criteriaBuilder));

            return predicate;
        };
    }
}
