package app.food.recommendation.endpoints;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.food.recommendation.models.User;
import app.food.recommendation.repositories.UserRepo;
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
	
	UserRepo
	userrepo;
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
	public String userbrands() {
				
	    return "user/brands";
	}
	
	
	@GetMapping("/contact")
	public String Contact() {

	    return "user/contact";
	}
	
	
	@GetMapping("/ourcollections")
	public String OurCollections() {

	    return "user/category";
	}
	

}
