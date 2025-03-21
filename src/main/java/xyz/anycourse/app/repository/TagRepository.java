package xyz.anycourse.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.anycourse.app.domain.entity.Tag;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    Page<Tag> findByNameContaining(String search, Pageable pageable);

    Optional<Tag> findByName(String name);

    Set<Tag> findByIdIn(Set<String> tagIds);
}