package app.food.recommendation.endpoints;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.food.recommendation.services.Services;
import lombok.AllArgsConstructor;
import lombok.Data;


@Controller
@Data
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	Services service ;
	
	
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
	
	

}
