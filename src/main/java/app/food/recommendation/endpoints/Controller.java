package app.food.recommendation.endpoints;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import app.food.recommendation.services.ServiceImp;
import lombok.AllArgsConstructor;
import lombok.Data;


@org.springframework.stereotype.Controller
@Data
@AllArgsConstructor
public class Controller {

	@Autowired
    ServiceImp service ;
	
//	@Autowired
//    SendEmailService SendEmailService;
//	
	public String CheckRole () {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	     
	    return authorities.toArray()[0].toString();
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
}
