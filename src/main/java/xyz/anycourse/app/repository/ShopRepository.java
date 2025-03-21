package xyz.anycourse.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import xyz.anycourse.app.domain.entity.Shop;
import xyz.anycourse.app.domain.entity.User;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, String>, JpaSpecificationExecutor<Shop> {
    Optional<Shop> findByIdAndOwner(String id, User owner);
}
