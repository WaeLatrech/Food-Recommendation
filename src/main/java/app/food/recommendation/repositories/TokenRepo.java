package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.ConfirmationToken;

public interface TokenRepo extends JpaRepository<ConfirmationToken, Long>{

}
