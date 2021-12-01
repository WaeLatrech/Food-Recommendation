package app.food.recommendation.endpoints;


import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.food.recommendation.models.Brand;
import app.food.recommendation.models.ConfirmationToken;
import app.food.recommendation.models.DishCategory;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.User;
import app.food.recommendation.repositories.DishCategoryRepo;
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
	
	UserRepo userrepo;
	TokenRepo tokenRepo;
	DishCategoryRepo dishCatRepo;
	
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
		
		
	    return "user/userindex";
	}
	
	
	@GetMapping("/brands")
	public String userbrands(Model model) {
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
	    return "user/brands";
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
		/**** footer *****/
//	    List <CategoryEntity> footerCategories = categories.subList(Math.max(categories.size() - 4, 0), categories.size());
//	    model.addAttribute("footercategories", footerCategories);
	    return "user/add-recipe";
	}
	
	@PostMapping("/add-recipe")
	public String registerSuccess( @RequestParam ("title") String title ,
			@RequestParam ("description") String description ,
			@RequestParam("ingredients") String ingredients,
			@RequestParam("preparationTime") String preparationTime ,
			@RequestParam("dishcategory") String dishcategory ,
			@RequestParam ("file") MultipartFile file ) {
		Recipe r = new Recipe();
		r.setPublisher(userrepo.findByUsername(getUserUsername()));
		r.setTitle(title);
		r.setDescription(description);
		r.setIngredients(ingredients);
		r.setPreparationTime(preparationTime);
		r.setDishcategory(dishCatRepo.findByDishcategory(dishcategory));
		
		
		String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	try {
    			r.setImgRecipe(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		service.createRecipe(r);
		return "redirect:/user/add-recipe";//+dishcategory;
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
		User user = userrepo.findByUsername(getUserUsername());
		model.addAttribute("user",user);
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
	

}
