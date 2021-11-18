package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Dish;

public interface DishRepo extends JpaRepository<Dish, Long>{

}
