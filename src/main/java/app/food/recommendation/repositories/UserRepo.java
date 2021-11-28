package app.food.recommendation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.food.recommendation.models.User;

public interface UserRepo extends JpaRepository<User, Long>{
    User findByEmail(String email);
    User findByUsername(String username);
}
