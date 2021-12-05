package app.food.recommendation.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Review;

public interface ReviewRepo  extends JpaRepository<Review,Long>{
	List<Review> findByRecipe(Recipe recipe);

}
