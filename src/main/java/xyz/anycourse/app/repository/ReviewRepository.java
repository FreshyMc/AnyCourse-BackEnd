package xyz.anycourse.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.anycourse.app.domain.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
}
