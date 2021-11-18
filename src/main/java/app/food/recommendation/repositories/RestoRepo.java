package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Restaurant;

public interface RestoRepo extends JpaRepository<Restaurant, Long>{

}
