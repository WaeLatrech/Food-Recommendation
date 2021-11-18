package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.Plan;

public interface PlanRepo extends JpaRepository<Plan, Long>{

}
