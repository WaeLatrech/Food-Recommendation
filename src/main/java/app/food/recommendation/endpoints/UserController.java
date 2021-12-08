package app.food.recommendation.endpoints;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
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
import app.food.recommendation.models.Review;
import app.food.recommendation.models.User;
import app.food.recommendation.repositories.DishCategoryRepo;
import app.food.recommendation.repositories.DislikeRepo;
import app.food.recommendation.repositories.LikeRepo;
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
	
	@GetMapping("/menu")
	public String Menu(Model model) {
 
//	    if (CheckRole().equals("USER")) {
//	        return "redirect:/user/home";
//	    }
//	    else if (CheckRole().equals("ADMIN")) {
//	        return "redirect:/admin/home";
//	    }
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    List<Recipe> recipes = service.getAllRecipes();
	    model.addAttribute("recipes", recipes);
	    
	    /***	Navbar	***/
	    List<DishCategory> dishcats = service.getAllDishCategories();
	    model.addAttribute("dishcategories", dishcats);
	    List <Category> categories = service.getAllCategories();
	    model.addAttribute("categories", categories);
	return "user/menu";
	}
	@GetMapping("/home")
	public String userindex(Model model,RedirectAttributes redirAttrs) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		redirAttrs.addFlashAttribute("error", "Mail not verified");
		return "redirect:/logout";
    	}
		
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
		
		
	    return "user/userindex";
	}
	
	
	@GetMapping("/brands")
	public String userbrands(Model model) {
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    return "user/brands";
	}
	
	
	
	
	@GetMapping("/contact")
	public String Contact(Model model) {
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
	public String Recipes(Model model) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		return "redirect:/logout";
    	}
		model.addAttribute("user", getUserUsername());
		model.addAttribute("user",userrepo.findByUsername(getUserUsername()));
	    List <Brand> brands = service.getAllBrands();
	    model.addAttribute("brands", brands);
	    List<Recipe> recipes = service.getAllRecipes();
	    model.addAttribute("recipes", recipes);
	return "user/recipes";
	}

	@GetMapping("/add-recipe")
	public String addProduct(Model model) {
				
		if (CheckRole().equals("NOTVERIFIED")) 
			{
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
	@GetMapping("/recipe/{id}")
	public String recipe(Model model,@PathVariable int id ) {
		if (CheckRole().equals("NOTVERIFIED")) 
		{
		return "redirect:/logout";
    	}
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user", user);
		Recipe recipe = service.getRecipeById(id);
		model.addAttribute("recipe",recipe);
		Review rev = new Review();
		model.addAttribute("review",rev);
		return "user/recipe";
		
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
