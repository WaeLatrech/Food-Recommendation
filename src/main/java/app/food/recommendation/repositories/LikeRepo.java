package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Like;

public interface LikeRepo  extends JpaRepository<Like,Long>{

}
