package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Dislike;

public interface DislikeRepo extends JpaRepository<Dislike,Long>{

}
