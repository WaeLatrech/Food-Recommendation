package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Brand;

public interface BrandRepo extends JpaRepository<Brand, Long>{

}
