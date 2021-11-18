package app.food.recommendation.services;

import java.util.List;

import app.food.recommendation.models.Category;
import app.food.recommendation.models.Dish;
import app.food.recommendation.models.Plan;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Restaurant;
import app.food.recommendation.models.User;


	public interface Services {
	////////////////**User**////////////
	List<User> getAllUsers();
	List<User> getUserByRole(String Role);
	User getUserById(long id);
	User createUser(User entity );
	User deleteUser(long id);
	User modifyUser(long id, User newUser);
	
	////////////////**Token**////////////
	void DelTokenByIdUser(long i);
	////////////////**Recipe**////////////
	List<Recipe> getAllRecipes();
	List<Recipe> getRecipesByCategory(String CategoryName);
	Recipe getRecipeById(long id);
	Recipe createRecipe(Recipe r );
	Recipe deleteRecipe(long id);
	Recipe modifyRecipe(long id, User newRecipe);
	////////////////**Category**////////////
	List<Category> getAllCategories();
	Category getCategoryById(long id);
	Category createCategory(Category Category);
	Category deleteCategory(long id);
	Category modifyCategory(long id, Category newCategory);
	////////////////**Restaurant**////////////
	List<Restaurant> getAllRestos();
	Restaurant getRestoById(long id);
	Restaurant createResto(Restaurant Restaurant);
	Restaurant deleteResto(long id);
	Restaurant modifyResto(long id, Restaurant newResto);
	////////////////**Dish**////////////
	List<Dish> getAllDishes();
	Dish getDishById(long id);
	Dish createDish(Dish d);
	Dish deleteDish(long id);
	Dish modifyDish(long id, Dish newDish);
	////////////////**Plan**////////////
	List<Plan> getAllPlans();
	Plan getPlanById(long id);
	Plan createPlan(Plan p );
	Plan deletePlan(long id);
	Plan modifyPlan(long id, Plan newPlan);
}
