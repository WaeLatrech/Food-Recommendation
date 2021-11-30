package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.DishCategory;

public interface DishCategoryRepo extends JpaRepository<DishCategory, Long>{
	DishCategory findByDishcategory(String dishcategory);

}
