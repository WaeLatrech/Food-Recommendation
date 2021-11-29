package app.food.recommendation.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.food.recommendation.models.Brand;
import app.food.recommendation.models.Category;
import app.food.recommendation.models.Dish;
import app.food.recommendation.models.Plan;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Restaurant;
import app.food.recommendation.models.User;
import app.food.recommendation.repositories.BrandRepo;
import app.food.recommendation.repositories.CategoryRepo;
import app.food.recommendation.repositories.DishRepo;
import app.food.recommendation.repositories.PlanRepo;
import app.food.recommendation.repositories.RecipeRepo;
import app.food.recommendation.repositories.RestoRepo;
import app.food.recommendation.repositories.TokenRepo;
import app.food.recommendation.repositories.UserRepo;
import app.food.recommendation.models.ConfirmationToken;


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
	
	@Autowired
	public ServiceImp(UserRepo repoUser, TokenRepo repoToken, RestoRepo repoResto, RecipeRepo repoRecipe,
			CategoryRepo repoCategory, DishRepo repoDish, PlanRepo repoPlan,BrandRepo repoBrand) {
		super();
		this.repoUser = repoUser;
		this.repoToken = repoToken;
		this.repoResto = repoResto;
		this.repoRecipe = repoRecipe;
		this.repoCategory = repoCategory;
		this.repoDish = repoDish;
		this.repoPlan = repoPlan;
		this.repoBrand = repoBrand;
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
		return repoRecipe.findAll();
	}

	@Override
	public List<Recipe> getRecipesByCategory(String CategoryName) {
		// TODO Auto-generated method stub
		return null;
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
	///////////////////////////////////////////////

	@Override
	public List<Restaurant> getAllRestos() {
		return repoResto.findAll();
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
	public Restaurant createResto(String brandname, String restoname, String location) {
		Restaurant resto = new Restaurant ();
		resto.setRestoname(restoname);
		resto.setLocation(location);
		resto.setBrandname(brandname);
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
	public Dish createDish(Dish d) {
		return repoDish.save(d);
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
		return repoBrand.findAll();
	}
	
	@Override
	public Brand getBrandById(int id) {
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
	public Brand deleteBrand(int id) {
		Brand brand = this.getBrandById(id);
		repoBrand.deleteById(id);
		return brand;
	}
	
	
	///////////////////////////////////////////////
}
