package xyz.anycourse.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.anycourse.app.domain.entity.Material;

import java.util.Set;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String> {
    Page<Material> findAllByShop_Id(String shopId, Pageable pageable);

    @Query(value = "SELECT m FROM Material m WHERE m.shop.id = :shopId AND m.id <> :materialId ORDER BY m.createdAt DESC")
    Page<Material> findRelatedMaterialsByShop(
        @Param("shopId") String shopId,
        @Param("materialId") String materialId,
        Pageable pageable
    );
}
