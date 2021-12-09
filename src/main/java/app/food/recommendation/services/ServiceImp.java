package app.food.recommendation.services;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import app.food.recommendation.models.Brand;
import app.food.recommendation.models.Category;
import app.food.recommendation.models.ConfirmationToken;
import app.food.recommendation.models.Dish;
import app.food.recommendation.models.DishCategory;
import app.food.recommendation.models.Plan;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Restaurant;
import app.food.recommendation.models.Review;
import app.food.recommendation.models.User;
import app.food.recommendation.repositories.BrandRepo;
import app.food.recommendation.repositories.CategoryRepo;
import app.food.recommendation.repositories.DishCategoryRepo;
import app.food.recommendation.repositories.DishRepo;
import app.food.recommendation.repositories.PlanRepo;
import app.food.recommendation.repositories.RecipeRepo;
import app.food.recommendation.repositories.RestoRepo;
import app.food.recommendation.repositories.ReviewRepo;
import app.food.recommendation.repositories.TokenRepo;
import app.food.recommendation.repositories.UserRepo;


@Service
public class ServiceImp implements Services{
	@Autowired
	SendEmailService SendEmailService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	private UserRepo repoUser;
	private TokenRepo repoToken;
	private RestoRepo repoResto;
	private RecipeRepo repoRecipe;
	private CategoryRepo repoCategory;
	private DishRepo repoDish;
	private PlanRepo repoPlan;
	private BrandRepo repoBrand;
	private DishCategoryRepo repoDishCat;
	private ReviewRepo repoReview;
	
	@Autowired
	public ServiceImp(UserRepo repoUser, TokenRepo repoToken, RestoRepo repoResto, RecipeRepo repoRecipe,
			CategoryRepo repoCategory, DishRepo repoDish, PlanRepo repoPlan,BrandRepo repoBrand
			,DishCategoryRepo repoDishCat, ReviewRepo repoReview) {
		super();
		this.repoUser = repoUser;
		this.repoToken = repoToken;
		this.repoResto = repoResto;
		this.repoRecipe = repoRecipe;
		this.repoCategory = repoCategory;
		this.repoDish = repoDish;
		this.repoPlan = repoPlan;
		this.repoBrand = repoBrand;
		this.repoDishCat = repoDishCat;
		this.repoReview = repoReview;
	}

	@Override
	public List<User> getAllUsers() {
		return repoUser.findAll();
	}

	@Override
	public List<User> getUserByRole(String Role) {
		List<User>	 user = repoUser.findAll();
		List<User>  userbyrole = new ArrayList<User>();
		for(User u : user )
		{
			if(u.getRole().equalsIgnoreCase(Role))
				userbyrole.add(u);
			
		}
	return userbyrole;
	}

	@Override
	public User getUserById(long id) {
		Optional<User> opt = repoUser.findById(id);
		User entity;
        if (opt.isPresent())
            entity = opt.get();
        else
            throw new NoSuchElementException("User with id : "+id+" is not found");
        return entity;
	}

	@Override
	public User createUser(User entity) {
       	entity.setRole("NOTVERIFIED");
       	entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
		 
        return repoUser.save(entity);
	}

	@Override
	public User deleteUser(long id) {
		User entity = this.getUserById(id);
        repoUser.deleteById(id);
        return entity;
	}

	@Override
	public User modifyUser(long id, User newUser) {
		User oldUser = this.getUserById(id);
        System.out.println("password 2 = '"+newUser.getPassword()+"'");	
       
        if (!newUser.getPassword().equals(""))
        {   
        	oldUser.setPassword(newUser.getPassword());
        	String password = bCryptPasswordEncoder.encode(newUser.getPassword());
        	oldUser.setPassword(password);
        }
        if (!newUser.getPhone().equals(""))    
            oldUser.setPhone(newUser.getPhone());
        if (newUser.getBirthDate() != null)
            oldUser.setBirthDate(newUser.getBirthDate());
        if (newUser.getRole() != null)
            oldUser.setRole(newUser.getRole());
        if (newUser.getImageU() != null)
            oldUser.setImageU(newUser.getImageU());
        
       return repoUser.save(oldUser);
	}

	@Override
	public void DelTokenByIdUser(long i) {
		List<ConfirmationToken> tokens = repoToken.findAll();
		for(ConfirmationToken t : tokens )
		{
			if(t.getUser().getId()==i)
				repoToken.deleteById(t.getTokenid());
		}		
	}
	public static String generateRandomPassword()
    { int len=10;
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*:!,.-_";
 
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
 
        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
 
        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
 
        return sb.toString();
    }
	///////////////////////////////////////////////
	
	@Override
	public List<Recipe> getAllRecipes() {
		List <Recipe> recipes = repoRecipe.findAll();
		Collections.reverse(recipes);
		return recipes;
	}
	@Override
	public List<Dish> getDishesByCategory(String cat) {
		List<Dish> dishes = new ArrayList<>();
		for (Dish d : repoDish.findAll()) {
			if(d.getDishcategory().getDishcategory().equalsIgnoreCase(cat))
				dishes.add(d);
		}
		return dishes;
	}
	@Override
	public List<Recipe> getRecipesByCategory(String cat) {
		List<Recipe> recipes = new ArrayList<>();
		for (Recipe r : repoRecipe.findAll()) {
			if(r.getDishcategory().getDishcategory().equalsIgnoreCase(cat))
				recipes.add(r);
		}
		return recipes;
	}
	@Override
	public List<Recipe> getRecipesBySearch(String s) {
		String search = s.toLowerCase();
		List<Recipe> recipes = new ArrayList<>();
		for (Recipe r : repoRecipe.findAll()) {
			if(r.getDishcategory().getDishcategory().toLowerCase().contains(search) 
					|| r.getIngredients().toLowerCase().contains(search)
					|| r.getDescription().toLowerCase().contains(search)
					|| r.getTitle().contains(search)
					|| r.getPublisher().getEmail().toLowerCase().contains(search)
					|| r.getPublisher().getUsername().toLowerCase().contains(search))
				recipes.add(r);
		}
		return recipes;
	}
	@Override
	public List<Dish> getDishesBySearch(String s) {
		String search = s.toLowerCase();
		List<Dish> dishes = new ArrayList<>();
		for (Dish r : repoDish.findAll()) {
			if(r.getDishcategory().getDishcategory().toLowerCase().contains(search) 
					|| r.getDishdescription().toLowerCase().contains(search)
					|| r.getDishname().contains(search)
					|| r.getResto().getRestoname().toLowerCase().contains(search))
				dishes.add(r);
		}
		return dishes;
	}

	@Override
	public Recipe getRecipeById(long id) {
		Optional<Recipe> opt = repoRecipe.findById(id);
		Recipe Recipe;
        if (opt.isPresent())
        	Recipe = opt.get();
        else
            throw new NoSuchElementException("Recipe with id : "+id+" is not found");
        return Recipe;
	}

	@Override
	public Recipe createRecipe(Recipe r) {
		return repoRecipe.save(r);
	}

	@Override
	public Recipe deleteRecipe(long id) {
		Recipe Recipe = this.getRecipeById(id);
        repoRecipe.deleteById(id);
        return Recipe;
	}

	@Override
	public Recipe modifyRecipe(long id, User newRecipe) {
		// TODO Auto-generated method stub
		return null;
	}
	///////////////////////////////////////////////

	@Override
	public List<DishCategory> getAllDishCategories() {
		return repoDishCat.findAll();
	}

	@Override
	public DishCategory getDishCategoryById(long id) {
		Optional<DishCategory> opt = repoDishCat.findById((long) id);
		DishCategory DishCategory;
        if (opt.isPresent())
        	DishCategory = opt.get();
        else
            throw new NoSuchElementException("DishCategory with id : "+id+" is not found");
        return DishCategory;
	}

	@Override
	public DishCategory createDishCategory(DishCategory DishCategory) {
		return repoDishCat.save(DishCategory);
	}

	@Override
	public DishCategory deleteDishCategory(long id) {
		DishCategory DishCategory = this.getDishCategoryById(id);
		repoDishCat.deleteById(id);
        return DishCategory;
	}

	@Override
	public DishCategory modifyDishCategory(long id, DishCategory DishCategory) {
		// TODO Auto-generated method stub
		return null;
	}
	///////////////////////////////////////////////
///////////////////////////////////////////////

	@Override
	public List<Category> getAllCategories() {
	return repoCategory.findAll();
	}
	
	@Override
	public Category getCategoryById(long id) {
	Optional<Category> opt = repoCategory.findById(id);
	Category Category;
	if (opt.isPresent())
	Category = opt.get();
	else
	throw new NoSuchElementException("Category with id : "+id+" is not found");
	return Category;
	}
	
	@Override
	public Category createCategory(Category Category) {
	return repoCategory.save(Category);
	}
	
	@Override
	public Category deleteCategory(long id) {
	Category Category = this.getCategoryById(id);
	repoCategory.deleteById(id);
	return Category;
	}

@Override
public Category modifyCategory(long id, Category newCategory) {
// TODO Auto-generated method stub
return null;
}
	@Override
	public List<Restaurant> getAllRestos() {
		List <Restaurant> restos = repoResto.findAll();
		Collections.reverse(restos);
		return restos;
	}
	@Override
	public List<Restaurant> getRestosByCategory(String cat) {
		List<Restaurant> restos = new ArrayList<>();
		for (Restaurant r : repoResto.findAll()) {
			if(r.getPlacecategory().getPlacecategory().equalsIgnoreCase(cat))
				restos.add(r);
		}
		return restos;
	}
	@Override
	public List<Restaurant> getRestosBySearch(String s) {
		String search = s.toLowerCase();
		List<Restaurant> restos = new ArrayList<>();
		for (Restaurant r : repoResto.findAll()) {
			if(r.getPlacecategory().getPlacecategory().toLowerCase().contains(search) 
					|| r.getBrandname().toLowerCase().contains(search)
					|| r.getRestoname().toLowerCase().contains(search)
					|| r.getLocation().toLowerCase().contains(search) )
				restos.add(r);
			else {
				for (Dish d : r.getMenu()) {
					String price = String.valueOf(d.getPrice());
					if(d.getDishname().toLowerCase().contains(search)
							|| d.getDishdescription().toLowerCase().contains(search)
							|| price.toLowerCase().contains(search))
						restos.add(r);
					
				}
			}
		}
		return restos;
	}
	@Override
	public List<Dish> getDishBySearch(String search,String location,float price) {
		String s = search.toLowerCase();
		String l = location.toLowerCase();
		List<Dish> dishes = repoDish.findAll();
		List<Dish> result = new ArrayList<Dish>();
		if(l.equals("empty") && price == 0) {
			for(Dish d : dishes) {
				if (d.getDishname().toLowerCase().contains(s)
					||d.getDishdescription().toLowerCase().contains(s)
					||d.getResto().getBrandname().toLowerCase().contains(s)
					||d.getResto().getRestoname().toLowerCase().contains(s) ){
					result.add(d);
				}
			}
			return result;
		}
		if(!l.equals("empty") && price == 0) {
			for(Dish d : dishes) {
				if ((d.getDishname().toLowerCase().contains(s)
					||d.getDishdescription().toLowerCase().contains(s)
					||d.getResto().getBrandname().toLowerCase().contains(s)
					||d.getResto().getRestoname().toLowerCase().contains(s))
					&& d.getResto().getLocation().toLowerCase().contains(l)){
					result.add(d);
				}
			}
			return result;
		}
		if(l.equals("empty") && price != 0) {
			for(Dish d : dishes) {
				if ((d.getDishname().toLowerCase().contains(s)
					||d.getDishdescription().toLowerCase().contains(s)
					||d.getResto().getBrandname().toLowerCase().contains(s)
					||d.getResto().getRestoname().toLowerCase().contains(s))
					&& d.getPrice()<=price){
					result.add(d);
				}
			}
			return result;
		}
		if(!l.equals("empty") && price != 0) {
			for(Dish d : dishes) {
				if ((d.getDishname().toLowerCase().contains(s)
					||d.getDishdescription().toLowerCase().contains(s)
					||d.getResto().getBrandname().toLowerCase().contains(s)
					||d.getResto().getRestoname().toLowerCase().contains(s))
					&& d.getResto().getLocation().toLowerCase().contains(l)
					&& d.getPrice()<=price){
					result.add(d);
				}
			}
			return result;
		}
		return result;

	}
	@Override
	public List<Recipe> getRecipeByADsearch(String i1,String i2,String i3) {
		String ing1 = i1.toLowerCase();
		String ing2 = i2.toLowerCase();
		String ing3 = i3.toLowerCase();
		List<Recipe> recipes = repoRecipe.findAll();
		List<Recipe> result = new ArrayList<Recipe>();
		
		if(!ing2.equals("empty") && ing3.equals("empty") ) {
			for(Recipe r : recipes) {
				if (r.getIngredients().toLowerCase().contains(ing2)
					&&r.getIngredients().toLowerCase().contains(ing1) ){
					result.add(r);
				}
			}
			return result;
		}
		if(ing2.equals("empty") && !ing3.equals("empty") ) {
			for(Recipe r : recipes) {
				if (r.getIngredients().toLowerCase().contains(ing3)
					&&r.getIngredients().toLowerCase().contains(ing1) ){
					result.add(r);
				}
			}
			return result;
		}
		if(!ing2.equals("empty") && !ing3.equals("empty") ) {
			for(Recipe r : recipes) {
				if (r.getIngredients().toLowerCase().contains(ing2)
					&&r.getIngredients().toLowerCase().contains(ing3)
					&&r.getIngredients().toLowerCase().contains(ing1) ){
					result.add(r);
				}
			}
			return result;
		}
		if(ing2.equals("empty") && ing3.equals("empty") ) {
			for(Recipe r : recipes) {
				if (r.getIngredients().toLowerCase().contains(ing1) ){
					result.add(r);
				}
			}
			return result;
		}
		return result;

	}
	@Override
	public Restaurant getRestoById(long id) {
		Optional<Restaurant> opt = repoResto.findById(id);
		Restaurant Restaurant;
        if (opt.isPresent())
        	Restaurant = opt.get();
        else
            throw new NoSuchElementException("Restaurant with id : "+id+" is not found");
        return Restaurant;
	}

	@Override
	public Restaurant createResto(String brandname, String restoname, String location,Category catplace) {
		Restaurant resto = new Restaurant ();
		resto.setRestoname(restoname);
		resto.setLocation(location);
		resto.setBrandname(brandname);
		resto.setPlacecategory(catplace);
		List<Brand> l = this.getAllBrands();
        for(Brand c : l)
        {
            if (resto.getBrandname().equalsIgnoreCase(c.getBrandname())) {
            	resto.setBrand(c);
                break;
            }
            
        }
        
    	
		return repoResto.save(resto);
	}

	@Override
	public Restaurant deleteResto(long id) {
		Restaurant Restaurant = this.getRestoById(id);
        repoResto.deleteById(id);
        return Restaurant;
	}

	@Override
	public Restaurant modifyResto(long id, Restaurant newResto) {
		// TODO Auto-generated method stub
		return null;
	}
	///////////////////////////////////////////////

	@Override
	public List<Dish> getAllDishes() {
		return repoDish.findAll();
	}

	@Override
	public Dish getDishById(long id) {
		Optional<Dish> opt = repoDish.findById(id);
		Dish Dish;
        if (opt.isPresent())
        	Dish = opt.get();
        else
            throw new NoSuchElementException("Dish with id : "+id+" is not found");
        return Dish;
	}

	@Override
	public Dish createDish(String restoname ,String dishcatname, String dishname, String dishdescription, float price,MultipartFile file) {
		Dish dish = new Dish ();
		dish.setDishcategory(repoDishCat.findByDishcategory(dishcatname));
		dish.setResto(repoResto.findByRestoname(restoname));
		dish.setDishdescription(dishdescription);
		dish.setPrice(price);
		dish.setDishname(dishname);
		
    	String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	try {
    		dish.setImageD(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        for(Restaurant c : this.getAllRestos())
        {
            if (dish.getResto().getRestoname().equalsIgnoreCase(c.getRestoname())) {
            	dish.setResto(c);
                break;
            }    
        }
        for(DishCategory c : this.getAllDishCategories())
        {
            if (dish.getDishcategory().getDishcategory().equalsIgnoreCase(c.getDishcategory())) {
            	dish.setDishcategory(c);
                break;
            }    
        }
        Restaurant rest = dish.getResto();
        List<Dish> menu = rest.getMenu();
        menu.add(dish);
        rest.setMenu(menu);
        repoResto.save(rest);
        for (Dish dish2 : rest.getMenu()) {
        	System.out.println("$$$$$$$$$$$"+dish2.getDishname());
		}
		return repoDish.save(dish);
	}


	@Override
	public Dish deleteDish(long id) {
		Dish Dish = this.getDishById(id);
        repoDish.deleteById(id);
        return Dish;
	}

	@Override
	public Dish modifyDish(long id, Dish newDish) {
		// TODO Auto-generated method stub
		return null;
	}
	///////////////////////////////////////////////

	@Override
	public List<Plan> getAllPlans() {
		return repoPlan.findAll();
	}

	@Override
	public Plan getPlanById(long id) {
		Optional<Plan> opt = repoPlan.findById(id);
		Plan Plan;
        if (opt.isPresent())
        	Plan = opt.get();
        else
            throw new NoSuchElementException("Plan with id : "+id+" is not found");
        return Plan;
	}

	@Override
	public Plan createPlan(Plan p) {
		return repoPlan.save(p);
	}

	@Override
	public Plan deletePlan(long id) {
		Plan Plan = this.getPlanById(id);
        repoPlan.deleteById(id);
        return Plan;
	}

	@Override
	public Plan modifyPlan(long id, Plan newPlan) {
		// TODO Auto-generated method stub
		return null;
	}

	
	///////////////////////////////////////////////
	
	@Override
	public List<Brand> getAllBrands() {
		List <Brand> brands = repoBrand.findAll();
		Collections.reverse(brands);
		return brands;
	}
	
	@Override
	public Brand getBrandById(long id) {
		Optional<Brand> opt = repoBrand.findById(id);
		Brand Brand;
		if (opt.isPresent())
			Brand = opt.get();
		else
			throw new NoSuchElementException("Brand with id : "+id+" is not found");
		return Brand;
	}
	
	@Override
	public Brand createBrand(Brand Brand) {
		return repoBrand.save(Brand);
	}
	
	@Override
	public Brand deleteBrand(long id) {
		Brand brand = this.getBrandById(id);
		repoBrand.deleteById(id);
		return brand;
	}

	@Override
	public Review createReview(long id, Review review) {
        review.setRecipe(getRecipeById(id));
        //review.setLikedBy(new ArrayList<>());
        //a.getLikedBy().add(0);
        //a1.setDislikedBy(new ArrayList<>());
        //a.getDislikedBy().add(0);
     
        repoReview.save(review);
        
        return review;
	}

	@Override
	public Review deleteReview(long id) {
		Review Review = this.getReviewById(id);
		Review.getRecipe().getReviews().remove(Review);
        repoReview.deleteById(id);
    	return Review;
	}

	@Override
	public List<Review> getAllReviewOfRecipe(long idrecipe) {
		return repoReview.findByRecipe(getRecipeById(idrecipe));
	}

	@Override
	public Review getReviewById(long id) {
		Optional<Review> opt = repoReview.findById(id);
        
		Review rev;
	        if (opt.isPresent())
	        	rev = opt.get();
	        else
	            throw new NoSuchElementException("Review with this id is not found");
	        return rev; 
	}

	@Override
	public Review modifyReview(long id, Review newReview) {
		Review oldReview = this.getReviewById(id);
        if (newReview.getNbdislike()!= 0 )
            oldReview.setNbdislike(newReview.getNbdislike());
        if (newReview.getNblike()!= 0 )
            oldReview.setNblike(newReview.getNblike());

        if (newReview.getComment()!= null )
            oldReview.setComment(newReview.getComment());
        if (newReview.getRepport()!= 0 )
            oldReview.setRepport(newReview.getRepport());
        
        
      return repoReview.save(oldReview);
	}
	
	
	///////////////////////////////////////////////
}
