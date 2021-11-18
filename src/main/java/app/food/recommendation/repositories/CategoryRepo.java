package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Category;


public interface CategoryRepo  extends JpaRepository<Category, Long>{

}
