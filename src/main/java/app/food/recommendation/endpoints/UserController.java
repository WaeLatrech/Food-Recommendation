package app.food.recommendation.endpoints;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	public String getUserUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username ;
		if (principal instanceof UserDetails) {
		 username = ((UserDetails)principal).getUsername();
		} else {
		 username = principal.toString();
		}
		return username;
	}
	
	
	@GetMapping("/home")
	public String userindex() {
				
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
