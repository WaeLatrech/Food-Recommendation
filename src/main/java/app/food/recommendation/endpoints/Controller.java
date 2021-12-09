package app.food.recommendation.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.food.recommendation.models.Brand;
import app.food.recommendation.models.Category;
import app.food.recommendation.models.ConfirmationToken;
import app.food.recommendation.models.Dish;
import app.food.recommendation.models.DishCategory;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Restaurant;
import app.food.recommendation.models.Review;
import app.food.recommendation.models.User;
import app.food.recommendation.repositories.ReviewRepo;
import app.food.recommendation.repositories.TokenRepo;
import app.food.recommendation.repositories.UserRepo;
import app.food.recommendation.services.SendEmailService;
import app.food.recommendation.services.ServiceImp;
import lombok.AllArgsConstructor;
import lombok.Data;


@org.springframework.stereotype.Controller
@Data
@AllArgsConstructor
public class Controller {

	@Autowired
    ServiceImp service ;
	
	@Autowired
    SendEmailService SendEmailService;

	private UserRepo userrepo;
	private TokenRepo tokenRepo;
	private ReviewRepo reviewRepo;
	
	public String CheckRole () {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	     
	    return authorities.toArray()[0].toString();
	}
	
	@GetMapping("/")
	public String returnindex(Model model) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    List <Review> AllReviews = reviewRepo.findAll();
	    Review review = new Review();
	    List <Review> reviews = AllReviews.subList(Math.max(AllReviews.size() - 9, 0), AllReviews.size());
	    model.addAttribute("reviews", reviews);
	    
	    List <Recipe> AllRecipes = service.getAllRecipes();
	    Collections.reverse(AllRecipes);
	    List <Recipe> NewRecipes = AllRecipes.subList(Math.max(AllRecipes.size() - 3, 0), AllRecipes.size());
	    model.addAttribute("NewRecipes", NewRecipes);
	    model.addAttribute("AllRecipes", AllRecipes);
	    List <Brand> brands = service.getAllBrands();
	    model.addAttribute("brands", brands);
	    Collections.reverse(brands);
	    List <Brand> newBrand = brands.subList(Math.max(brands.size() - 4, 0), brands.size());
	    model.addAttribute("newBrand", newBrand);

	    /**** average ****/
	    List<app.food.recommendation.models.User> users = service.getAllUsers() ; 
	    model.addAttribute("users", users);
	    
	    List<Restaurant> restos = service.getAllRestos() ; 
	    model.addAttribute("restos", restos);
	    
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    
	    return "index";
	}
	@GetMapping("/menu")
	public String Menu(Model model) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    
	    List<Recipe> recipes = service.getAllRecipes();
	    model.addAttribute("recipes", recipes);
	    
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/menu";
	}
	@GetMapping("/recipes")
	public String Recipes(Model model) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    
	    List<Recipe> recipes = service.getAllRecipes();
	    model.addAttribute("recipes", recipes);
	    
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/recipes";
	}
	@GetMapping("/recipes/{category}")
	public String RecipesByCategory(Model model,@PathVariable String category ) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    List<Recipe> recipes = service.getRecipesByCategory(category);
	    model.addAttribute("recipes", recipes);


	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/recipes";
	}
	
	@GetMapping("/searchRecipe")
	public String RecipesSearch(Model model,@ModelAttribute("search") String search) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    List<Recipe> recipes = service.getRecipesBySearch(search);
	    model.addAttribute("recipes", recipes);

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/recipes";
	}
	
	@GetMapping("/recipe/{id}")
	public String recipe(Model model,@PathVariable int id ) {
	    
		Recipe recipe = service.getRecipeById(id);
		model.addAttribute("recipe",recipe);
		Review rev = new Review();
		model.addAttribute("review",rev);
		
		/***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
		return "Other/recipe";
		
	}
	@GetMapping("/contact")
	public String Contact(Model model) {

	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/Contact";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "Other/contact";
	}
	@PostMapping("/Contact")
	public String ContactMail(@RequestParam("email") String to,@RequestParam("message") String body, @RequestParam("subject") String topic,@RequestParam("name") String name) {
		System.out.println("Sending : "+to+" "+body+" "+topic);
		SendEmailService.sendEmail(to,body,"By "+name+": "+topic);
		System.out.println(" : "+to+" "+body+" "+topic);
	    return "Other/contact";
	}
	@GetMapping("/brands")
	public String Brands(Model model) {

	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/brands";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/brands";
	    }
	    List<Brand> brands = service.getAllBrands();
	    model.addAttribute("brands",brands);
	    model.addAttribute("nbrRestos",brands);
	    
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "Other/brands";
	}

	@GetMapping("/restos/{id}")
	public String restos(Model model,@PathVariable long id ) {

	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/brands";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/brands";
	    }
	    List<Restaurant> restos = service.getBrandById(id).getRestos();
	    model.addAttribute("restos",restos);

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "Other/restos";
	}
	@GetMapping("/searchResto")
	public String RestoSearch(Model model,@ModelAttribute("search") String search) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    List<Restaurant> restos = service.getRestosBySearch(search);
	    model.addAttribute("restos", restos);

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "Other/restos";
	}

	@GetMapping("/restos/cat/{category}")
	public String RestosByCategory(Model model,@PathVariable String category ) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    List<Restaurant> restos = service.getRestosByCategory(category);
	    model.addAttribute("restos", restos);


	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/restos";
	}
	@GetMapping("/menu/{id}")
	public String Menu(Model model,@PathVariable int id) {
 
//	    if (CheckRole().equals("USER")) {
//	        return "redirect:/user/home";
//	    }
//	    else if (CheckRole().equals("ADMIN")) {
//	        return "redirect:/admin/home";
//	    }
		
	    List<Recipe> recipes = service.getAllRecipes();
	    model.addAttribute("recipes", recipes);
	    
	    Restaurant resto = service.getRestoById(id);
	    model.addAttribute("resto",resto);
	    
	    List<DishCategory> dg = new ArrayList<>();
	    System.out.println("$$$$$$$$$$");
	    for (Dish dish : resto.getMenu()) {
			if (!dg.contains(dish.getDishcategory()))
				{dg.add(dish.getDishcategory());System.out.println("$$$$$$$$$$"+dish.getDishcategory().getDishcategory());}
		}
	    model.addAttribute("dishCategoryList",dg);
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/menu";
	}
	@GetMapping("/dishes")
	public String Dishes(Model model) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/dishes";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/dishes";
	    }
		
	    List<Dish> dishes = service.getAllDishes();
	    model.addAttribute("dishes",dishes);
	    
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/dishes";
	}
	@GetMapping("/searchDish")
	public String DishsSearch(Model model,@ModelAttribute("search") String search) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    List<Dish> dishes = service.getDishesBySearch(search);
	    model.addAttribute("dishes", dishes);

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/dishes";
	}
	@GetMapping("/dishes/{category}")
	public String DishesByCategory(Model model,@PathVariable String category ) {
 
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    List<Dish> dishes = service.getDishesByCategory(category);
	    model.addAttribute("dishes", dishes);


	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "Other/dishes";
	}
	@GetMapping("/advancedDishSearch")
	public String RestoSearchAdvanced(Model model) {
 
//	    if (CheckRole().equals("USER")||CheckRole().equals("ADMIN")) {
//	        return "redirect:/user/searchRestoAdvanced";
//	    }

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "Other/searchDish";
	}
	@GetMapping("/advancedDish")
	public String RestoSearchAdvanced1(Model model,
			@RequestParam ("search") String search ,  
			@RequestParam ("location") String location,
			@RequestParam ("price") String price) {
 
//	    if (CheckRole().equals("USER")||CheckRole().equals("ADMIN")) {
//	        return "redirect:/user/searchRestoAdvanced";
//	    }
		if (price.isEmpty())
			price = "0";
		float p = Float.parseFloat(price);
		if(location.isEmpty())
			location="empty";
		
		List<Dish> dishes = service.getDishBySearch(search, location, p);
	    model.addAttribute("dishes", dishes);
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "Other/dishes";
	}
	@RequestMapping("/default")
	public String defaultAfterLogin() {

	    String myRole=CheckRole();
	    if (myRole.equals("USER")||myRole.equals("NOTVERIFIED")) {
	        return "redirect:/user/home";
	    }else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }
	    
	    return "redirect:/admin/home";
	}
	
	@GetMapping("/Login")
	public String login(Model model) {

	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }
	    else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "Other/login";
	}
	
	
	@GetMapping("/logout-Success")
	public String logout() {
	    return "redirect:/Login";
	}
	
	@GetMapping("/Sign-up")
	public String SignUp(Model model) {
	    
	    if (CheckRole().equals("USER")) {
	        return "redirect:/user/home";
	    }else if (CheckRole().equals("ADMIN")) {
	        return "redirect:/admin/home";
	    }

		User user = new User();
		model.addAttribute("user",user);

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
		return "Other/Sign-up";
	}
	
	@PostMapping("/Sign-up")
	public String UserregisterSuccess(@ModelAttribute("user") User user, RedirectAttributes redirAttrs, @RequestParam("file") MultipartFile file) {
		String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	try {
    			user.setImageU(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			System.out.println("dowiw");
			e.printStackTrace();
		}
		User existingMail = userrepo.findByEmail(user.getEmail());
		User existingUsername = userrepo.findByUsername(user.getUsername());
        if(existingMail != null)
        {	redirAttrs.addFlashAttribute("error", "mail already exists");
        	return "redirect:/Sign-up";
        }
        else if(existingUsername != null)
        {	redirAttrs.addFlashAttribute("error", "Username already exists");
        	return "redirect:/Sign-up";
        }
        else
        {
        	service.createUser(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            tokenRepo.save(confirmationToken);
            String text="To confirm your account, please click here : "
                    +"http://localhost:9090/confirm-account/"+confirmationToken.getConfirmationToken();
            SendEmailService.verifyEmail(user.getEmail(),"Mail Verified!",text);
           redirAttrs.addFlashAttribute("success", "Account created! Check your mail to activate Your Account");
            return "redirect:/Login";
  }

	}
	
	@GetMapping("/confirm-account/{confirmationToken}")
    public String confirmUserAccount(@PathVariable String confirmationToken, RedirectAttributes redirAttrs)
    {	
        ConfirmationToken token = tokenRepo.findByConfirmationToken(confirmationToken);
        if (token.getExpired()==1)
        	return "redirect:/Login";
        
        User user = userrepo.findByEmail(token.getUser().getEmail());
        user.setRole("USER");
		userrepo.save(user);
		SendEmailService.welcomeMail(user.getEmail(),user.getUsername());
		token.setExpired(1);
		tokenRepo.save(token);
		redirAttrs.addFlashAttribute("success", "Account Activated! Try to Login");
		return "redirect:/Login";

    }
	
	@GetMapping("/forgotpass")
	public String forgotpass(Model model) {
		
	    return "Other/forgot-password";
	}
	@PostMapping("/forgotpass")
	public String forgotpass1(@RequestParam("email") String email, RedirectAttributes redirAttrs) {
		User user = userrepo.findByEmail(email);
        if(user == null)
        {	
        redirAttrs.addFlashAttribute("error", "email doesn't exist");
        return "redirect:/forgotpass";
        }
        else
        {	//lazem na3mlou table o5ra mta3 tokens teb3a el password
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            tokenRepo.save(confirmationToken);
            String text="To Change your password, please click here : "
                    +"http://localhost:9090/change-password/"+confirmationToken.getConfirmationToken();
            SendEmailService.changePassword(email,"Change Password !",text);
    		redirAttrs.addFlashAttribute("success", "Check Your Mail to Confirm new Password");

            return "redirect:/Login";
        }
	}
	@GetMapping("/change-password/{confirmationToken}")
	public String updPassword(Model model,@PathVariable String confirmationToken) {
		 ConfirmationToken token = tokenRepo.findByConfirmationToken(confirmationToken);
		 if (token.getExpired()==1)
	        	return "redirect:/Login";
		 User user = userrepo.findByEmail(token.getUser().getEmail());
			model.addAttribute("user",user);
			model.addAttribute("token",token);
			return "Other/forgotpass_part2";
		
	}
	
	@PostMapping("/change-password/{confirmationToken}")
	public String updPassword1(@ModelAttribute("user") User user ,@PathVariable String confirmationToken, RedirectAttributes redirAttrs) {	
        ConfirmationToken token = tokenRepo.findByConfirmationToken(confirmationToken);

        User olduser = userrepo.findByEmail(token.getUser().getEmail());
        olduser.setPassword(user.getPassword());
        System.out.println("Passworrrrd = "+olduser.getPassword());
        service.modifyUser(olduser.getId(), olduser);
        token.setExpired(1);
        tokenRepo.save(token);
		redirAttrs.addFlashAttribute("success", "Password Modified Successfully");

		return "redirect:/Login";
	}
	
}
