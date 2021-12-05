package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Review;

public interface ReviewRepo  extends JpaRepository<Review,Long>{

}
