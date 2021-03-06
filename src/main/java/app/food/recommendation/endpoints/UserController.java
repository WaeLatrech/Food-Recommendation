package app.food.recommendation.endpoints;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
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
import app.food.recommendation.models.Dislike;
import app.food.recommendation.models.Like;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Restaurant;
import app.food.recommendation.models.Review;
import app.food.recommendation.models.User;
import app.food.recommendation.repositories.DishCategoryRepo;
import app.food.recommendation.repositories.DislikeRepo;
import app.food.recommendation.repositories.LikeRepo;
import app.food.recommendation.repositories.ReviewRepo;
import app.food.recommendation.repositories.TokenRepo;
import app.food.recommendation.repositories.UserRepo;
import app.food.recommendation.services.SendEmailService;
import app.food.recommendation.services.ServiceImp;
import lombok.AllArgsConstructor;
import lombok.Data;


@Controller
@Data
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	ServiceImp service ;
	@Autowired
    SendEmailService SendEmailService;
	
	private UserRepo userrepo;
	private TokenRepo tokenRepo;
	private DishCategoryRepo dishCatRepo;
	private LikeRepo likeRepo;
	private DislikeRepo dislikeRepo;
	private ReviewRepo reviewRepo;
	
	public String CheckRole () {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	     
	    return authorities.toArray()[0].toString();
	}
	
	public String getUserUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username ;
		if (principal instanceof UserDetails) {
		 username = ((UserDetails)principal).getUsername();
		} else {
		 username = principal.toString();
		}
		User user = userrepo.findByEmail(username);
		return user.getUsername();
	}
	public void setUserUsername(String username,String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
			}
	//////////////////////////////////
	
	
	
	@GetMapping("/home")
	public String userindex(Model model,RedirectAttributes redirAttrs) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		
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
	    
		
	    return "user/userindex";
	}
	
	@GetMapping("/menu/{id}")
	public String Menu(Model model,@PathVariable int id,RedirectAttributes redirAttrs) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
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
	return "user/menu";
	}
	@GetMapping("/brands")
	public String userbrands(Model model,RedirectAttributes redirAttrs) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		
		List<Brand> brands = service.getAllBrands();
	    model.addAttribute("brands",brands);
	    model.addAttribute("nbrRestos",brands);
	    
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "user/brands";
	}
	@GetMapping("/restos/{id}")
	public String restos(Model model,@PathVariable long id ,RedirectAttributes redirAttrs) {

		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		
	    List<Restaurant> restos = service.getBrandById(id).getRestos();
	    model.addAttribute("restos",restos);

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "user/restos";
	}
	@GetMapping("/searchResto")
	public String RestoSearch(Model model,@ModelAttribute("search") String search,RedirectAttributes redirAttrs) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		
	    List<Restaurant> restos = service.getRestosBySearch(search);
	    model.addAttribute("restos", restos);
	    for (Restaurant restaurant : restos) {
			System.out.println("$$$$"+restaurant.getRestoname());
		}
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "user/restos";
	}

	@GetMapping("/restos/cat/{category}")
	public String RestosByCategory(Model model,@PathVariable String category ,RedirectAttributes redirAttrs) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		
	    List<Restaurant> restos = service.getRestosByCategory(category);
	    model.addAttribute("restos", restos);


	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/restos";
	}
	/****** Dishes ********/
	@GetMapping("/dishes")
	public String Dishes(Model model,RedirectAttributes redirAttrs) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    List<Dish> dishes = service.getAllDishes();
	    model.addAttribute("dishes",dishes);
	    
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/dishes";
	}
	@GetMapping("/searchDish")
	public String DishsSearch(Model model,@ModelAttribute("search") String search,RedirectAttributes redirAttrs) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    List<Dish> dishes = service.getDishesBySearch(search);
	    model.addAttribute("dishes", dishes);

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/dishes";
	}
	@GetMapping("/dishes/{category}")
	public String DishesByCategory(Model model,@PathVariable String category ,RedirectAttributes redirAttrs) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    List<Dish> dishes = service.getDishesByCategory(category);
	    model.addAttribute("dishes", dishes);


	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/dishes";
	}
	@GetMapping("/advancedDishSearch")
	public String RestoSearchAdvanced(Model model,RedirectAttributes redirAttrs) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	    return "user/searchDish";
	}
	@GetMapping("/advancedDish")
	public String RestoSearchAdvanced1(Model model,RedirectAttributes redirAttrs,
			@RequestParam ("search") String search ,  
			@RequestParam ("location") String location,
			@RequestParam ("price") String price) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		
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
	    return "user/dishes";
	}
	
	
	
	@GetMapping("/contact")
	public String Contact(Model model,RedirectAttributes redirAttrs) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    return "user/contact";
	}
	
	
	@GetMapping("/ourcollections")
	public String OurCollections(Model model) {
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    return "user/category";
	}
	
	@GetMapping("/Account")
	public String Account(Model model) {
		model.addAttribute("user",userrepo.findByUsername(getUserUsername()));
	    return "user/Account";
	}

	@PostMapping("/upd_account")
	public String EditAccount( Model model ,@RequestParam ("username") String username ,
			@RequestParam ("email") String email , @RequestParam("password") String password,
			@RequestParam("phone") String phone,@RequestParam ("birthDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date birthDate  ,@RequestParam("file") MultipartFile file, 
			RedirectAttributes redirAttrs) {
		User olduser = userrepo.findByUsername(getUserUsername());
		User newuser =new User();
			String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
	    	if(FileName.contains("..")) {
	    		System.out.println("not a proper file ");
	    	}
	    	try {
	    		if(!FileName.isEmpty()) {
	    			newuser.setImageU(Base64.getEncoder().encodeToString(file.getBytes()));
					System.out.println("cv");
	    		}
	    		else {
	    			newuser.setImageU(olduser.getImageU());
	    		}
						} catch (IOException e) {
				e.printStackTrace();
			}
		 newuser.setUsername(username);
		 newuser.setEmail(email);
			User existingMail = userrepo.findByEmail(newuser.getEmail());
			User existingUsername = userrepo.findByUsername(newuser.getUsername());
	        if((existingMail != null)&&(existingMail != olduser))
	        {	
	        	redirAttrs.addFlashAttribute("error", "Mail already exists");
	        	return "redirect:/user/Account";
	        }
	        else if((existingUsername != null)&&(existingUsername != olduser))
	        {	
	        	redirAttrs.addFlashAttribute("error", "Username already exists");
	        	return "redirect:/user/Account";
	        }
	        else
	        {
		 newuser.setPassword(password);
		 newuser.setPhone(phone);
		 newuser.setBirthDate(birthDate);
		 service.modifyUser(olduser.getId(), newuser);
		 System.out.println("$$$$$");
		 System.out.println(newuser.getUsername());
		 System.out.println(newuser.getPassword());
		 System.out.println("$$$$$");	
		 setUserUsername(newuser.getUsername(), newuser.getPassword());
		 
		 if (!newuser.getEmail().equals(olduser.getEmail())) {

			 System.out.println("$$$$$$$$$$$test1");
			 ConfirmationToken confirmationToken = new ConfirmationToken(olduser);
	            tokenRepo.save(confirmationToken);
	            String text="To confirm your email, please click here : "
	                    +"http://localhost:9090/confirm-Email/"+confirmationToken.getConfirmationToken()+"/"
	                    +newuser.getEmail();
	            SendEmailService.verifyEmail(newuser.getEmail(),"Mail Verified!",text);
	           redirAttrs.addFlashAttribute("success", "Email Changed! Check your mail to Verify it");
	            return "redirect:/Login";
		 }
		 System.out.println("$$$$$$$$$$$test");
		return "redirect:/user/Account";
		}
	}
	/*********************recipes******************/
	@GetMapping("/recipes")
	public String Recipes(RedirectAttributes redirAttrs,Model model) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		model.addAttribute("user",userrepo.findByUsername(getUserUsername()));
	    List<Recipe> recipes = service.getAllRecipes();
	    model.addAttribute("recipes", recipes);
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/recipes";
	}
	@GetMapping("/recipes/{category}")
	public String RecipesByCategory(RedirectAttributes redirAttrs,Model model,@PathVariable String category ) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
	    List<Recipe> recipes = service.getRecipesByCategory(category);
	    model.addAttribute("recipes", recipes);
		model.addAttribute("user",userrepo.findByUsername(getUserUsername()));


	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/recipes";
	}
	@GetMapping("/advancedSearchRecipe")
	public String AdvancedSearchRecipe(RedirectAttributes redirAttrs,Model model) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		model.addAttribute("user",userrepo.findByUsername(getUserUsername()));

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/searchRecipe";
	}
	@GetMapping("/advancedRecipe")
	public String RecipesSearchAdvanced(RedirectAttributes redirAttrs,Model model,
			@ModelAttribute("ing1") String ing1,
			@ModelAttribute("ing2") String ing2,
			@ModelAttribute("ing3") String ing3) {
 
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
	    if(ing2.isEmpty())
	    	ing2="empty";
	    if(ing3.isEmpty())
	    	ing3="empty";
	    List<Recipe> recipes = service.getRecipeByADsearch(ing1, ing2, ing3);
	    model.addAttribute("recipes", recipes);
		model.addAttribute("user",userrepo.findByUsername(getUserUsername()));

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/recipes";
	}
	@GetMapping("/searchRecipe")
	public String RecipesSearch(RedirectAttributes redirAttrs,Model model,
			@ModelAttribute("search") String search) {
		
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		model.addAttribute("user",userrepo.findByUsername(getUserUsername()));
		List<Recipe> recipes = service.getRecipesBySearch(search);
	    model.addAttribute("recipes", recipes);

	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/recipes";
	}
	
	@GetMapping("/recipe/{id}")
	public String recipe(RedirectAttributes redirAttrs,Model model,@PathVariable int id ) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		model.addAttribute("user",userrepo.findByUsername(getUserUsername()));

		Recipe recipe = service.getRecipeById(id);
		model.addAttribute("recipe",recipe);
		Review rev = new Review();
		model.addAttribute("review",rev);
		
		/***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
		return "user/recipe";
		
	}
	@GetMapping("/add-recipe")
	public String addProduct(RedirectAttributes redirAttrs,Model model) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		
		List<Brand> brands = service.getAllBrands();
		model.addAttribute("brands",brands);
		
		List<DishCategory> dishcategory = service.getAllDishCategories();
		model.addAttribute("dishcategory",dishcategory);
		
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		List<Dish> dishes = new ArrayList<>();
		model.addAttribute("dishes",dishes);
		/**** footer *****/
//	    List <CategoryEntity> footerCategories = categories.subList(Math.max(categories.size() - 4, 0), categories.size());
//	    model.addAttribute("footercategories", footerCategories);
	    return "user/add-recipe";
	}
	
	@PostMapping("/add-recipe")
	public String registerSuccess(RedirectAttributes redirAttrs,
			@RequestParam ("title") String title ,
			@RequestParam ("description") String description ,
			@RequestParam("ingredients") String ingredient,
			@RequestParam("preparationTime") String preparationTime ,
			@RequestParam("dishcategory") String dishcategory ,
			@RequestParam ("file") MultipartFile file ) {

		Recipe r = new Recipe();
		r.setPublisher(userrepo.findByUsername(getUserUsername()));
		r.setTitle(title);
		r.setDescription(description);
		r.setIngredients(ingredient);
		r.setPreparationTime(preparationTime);
		r.setDishcategory(dishCatRepo.findByDishcategory(dishcategory));
		//Map<String,Float> ings = new HashMap<String,Float>();
		//ings.put(ingredient, Float.parseFloat(quantity));
		//r.setIngredients(ings);
		
		
		String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	if(!file.isEmpty()) {
    	try {
    			r.setImgRecipe(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	}

		service.createRecipe(r);
		return "redirect:/user/recipe/"+r.getIdrecipe();
	}

	@PostMapping("/add-review/{id}")
    public String ReviewSuccess(@ModelAttribute("review") Review review ,
    		@PathVariable("id") int idrecipe, @RequestParam("file") MultipartFile file ) {
        review.setUser(userrepo.findByUsername(getUserUsername()));
        String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	if (!file.isEmpty())
    	{
    	try {
    			review.setImgReview(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	}

        try {
            service.createReview(idrecipe, review);
        }
        catch(NoSuchElementException e) {
            return "user/RecipeNotFound";
        }
        return "redirect:/user/recipe/"+idrecipe;
    }
	
	@GetMapping("/like/{idrecipe}/{idreview}/{userid}")
	public String Like (RedirectAttributes redirAttrs,
			@PathVariable("idreview") long idreview ,
			@PathVariable("userid") long iduser ,
			@PathVariable("idrecipe") long idrecipe ) {
		Review review = service.getReviewById(idreview);
		List <Like> likes = likeRepo.findAll();
		for (Like i : likes) {
			if((i.getIdreview()==idreview)&&(i.getIduser()==iduser)) {
				review.setNblike(review.getNblike()-1);
				service.modifyReview(idreview, review);
				likeRepo.delete(i);
				return "redirect:/user/recipe/"+idrecipe;
			}}
		
			for (Dislike j : dislikeRepo.findAll()) {
			
			 if ((j.getIdreview()==idreview)&&(j.getIduser()==iduser)) {
				 	review.setNbdislike(review.getNbdislike()-1);
					review.setNblike(review.getNblike()+1);
				service.modifyReview(idreview, review);
				dislikeRepo.delete(j);
			Like like = new Like(iduser,idreview);
			likeRepo.save(like);
				return "redirect:/user/recipe/"+idrecipe;
			}

			}
				
		review.setNblike(review.getNblike()+1);
		service.modifyReview(idreview, review);
	Like like = new Like(iduser,idreview);
	likeRepo.save(like);
	return "redirect:/user/recipe/"+idrecipe;
}
	@GetMapping("/dislike/{idrecipe}/{idreview}/{userid}")
	public String DisLike (RedirectAttributes redirAttrs,
			@PathVariable("idreview") long idreview ,
			@PathVariable("userid") long iduser ,
			@PathVariable("idrecipe") long idrecipe ) {
		Review review = service.getReviewById(idreview);
		for (Dislike i : dislikeRepo.findAll()) {
			if((i.getIdreview()==idreview)&&(i.getIduser()==iduser)) {
				review.setNbdislike(review.getNbdislike()-1);
				service.modifyReview(idreview, review);
				dislikeRepo.delete(i);
				return "redirect:/user/recipe/"+idrecipe;
			}}
				
			for (Like j : likeRepo.findAll()) {
			
			 if ((j.getIdreview()==idreview)&&(j.getIduser()==iduser)) {
				 review.setNblike(review.getNblike()-1);
				 review.setNbdislike(review.getNbdislike()+1);
				service.modifyReview(idreview, review);
				likeRepo.delete(j);				
			Dislike dislike = new Dislike(iduser,idreview);
			dislikeRepo.save(dislike);
				return "redirect:/user/recipe/"+idrecipe;
			}

			}
			
		
		review.setNbdislike(review.getNbdislike()+1);
		service.modifyReview(idreview, review);
	Dislike dislike = new Dislike(iduser,idreview);
	dislikeRepo.save(dislike);
	return "redirect:/user/recipe/"+idrecipe;
}

}
