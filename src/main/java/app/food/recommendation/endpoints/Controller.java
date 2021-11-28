package app.food.recommendation.endpoints;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
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

import app.food.recommendation.models.ConfirmationToken;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Restaurant;
import app.food.recommendation.models.User;
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

	UserRepo userrepo;
	TokenRepo tokenRepo;
	
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
	    
	    List <Restaurant> restos = service.getAllRestos();
	    //product => resto
	    Restaurant resto = new Restaurant();
	    List <Restaurant> newResto = restos.subList(Math.max(restos.size() - 3, 0), restos.size());
	    model.addAttribute("restos", restos);
	    model.addAttribute("newResto", newResto);
	  //AvisEntity => reciepe
	    List <Recipe> AllRecipes = service.getAllRecipes();
	    Recipe recipe = new Recipe();
	    List <Recipe> NewRecipes = AllRecipes.subList(Math.max(AllRecipes.size() - 9, 0), AllRecipes.size());
	    model.addAttribute("NewRecipes", NewRecipes);
	    model.addAttribute("AllRecipes", AllRecipes);
	    /**** average ****/
	    List<app.food.recommendation.models.User> users = service.getAllUsers() ; 
	    model.addAttribute("users", users);
	    
//	    List<CategoryEntity> categories = service.getAllCategories() ; 
//	    model.addAttribute("categories", categories);
//	    model.addAttribute("reviewss" , AllReviews)  ; 
//	    model.addAttribute("products", products);
//	    
//	    /**** footer *****/
//	    List <CategoryEntity> footerCategories = categories.subList(Math.max(categories.size() - 4, 0), categories.size());
//	    model.addAttribute("footercategories", footerCategories);
	    
	    return "/index";
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
//	    List<CategoryEntity> categories = service.getAllCategories() ; 
//	    model.addAttribute("categories", categories);
//	    /**** footer *****/
//	    List <CategoryEntity> footerCategories = categories.subList(Math.max(categories.size() - 4, 0), categories.size());
//	    model.addAttribute("footercategories", footerCategories);
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
//	    List<CategoryEntity> categories = service.getAllCategories() ; 
//	    model.addAttribute("categories", categories);
		User user = new User();
		model.addAttribute("user",user);
	    /**** footer *****/
//	    List <CategoryEntity> footerCategories = categories.subList(Math.max(categories.size() - 4, 0), categories.size());
//	    model.addAttribute("footercategories", footerCategories);
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
	
	
}
