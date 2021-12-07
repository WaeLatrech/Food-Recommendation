package app.food.recommendation.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import app.food.recommendation.models.Brand;
import app.food.recommendation.models.Category;
import app.food.recommendation.models.Dish;
import app.food.recommendation.models.DishCategory;
import app.food.recommendation.models.Plan;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Restaurant;
import app.food.recommendation.models.Review;
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
	List<Recipe> getRecipesByCategory(String cat);
	List<Recipe> getRecipesBySearch(String search);
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
	////////////////**Category**////////////
	List<DishCategory> getAllDishCategories();
	DishCategory getDishCategoryById(long id);
	DishCategory createDishCategory(DishCategory DishCategory);
	DishCategory deleteDishCategory(long id);
	DishCategory modifyDishCategory(long id, DishCategory DishCategory);
	////////////////**Restaurant**////////////
	List<Restaurant> getAllRestos();
	Restaurant getRestoById(long id);
	Restaurant createResto(String brand, String restoname, String location);
	Restaurant deleteResto(long id);
	Restaurant modifyResto(long id, Restaurant newResto);
	////////////////**Dish**////////////
	List<Dish> getAllDishes();
	Dish getDishById(long id);
	Dish createDish(String restoname, String dishcatname, String dishname, String dishdescription, float price,MultipartFile file);
	Dish deleteDish(long id);
	Dish modifyDish(long id, Dish newDish);
	////////////////**Plan**////////////
	List<Plan> getAllPlans();
	Plan getPlanById(long id);
	Plan createPlan(Plan p );
	Plan deletePlan(long id);
	Plan modifyPlan(long id, Plan newPlan);
	
	////////////////**Brand**////////////
	List<Brand> getAllBrands();
	Brand getBrandById(long id);
	Brand createBrand(Brand Brand);
	Brand deleteBrand(long id);
	
	////////////////**Review**////////////
    Review createReview(long id, Review a ) ;
    Review deleteReview( long id ) ; 
    List<Review> getAllReviewOfRecipe(long idrecipe);
    Review getReviewById( long id) ; 
    Review modifyReview( long id , Review newEntityAvis);
//    public Review addLike(int id , long userid);
    //public Review addDisLike(int id , int userid);
//	public List<ReportEntity> getAllReports();
//	public List<LikeEntity> getAllLikes();
//	public List<DislikeEntity> getAllDisLikes();
	
}
