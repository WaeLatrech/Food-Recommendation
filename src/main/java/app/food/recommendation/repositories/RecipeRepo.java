package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Recipe;

public interface RecipeRepo extends JpaRepository<Recipe, Long>{

}
