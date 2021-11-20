package app.food.recommendation.endpoints;

import java.util.Collection;

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
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	Services service ;

    
	@GetMapping("/home")
	public String returnindexadmin() {
		
	    return "admin/indexadmin";
	}
}
