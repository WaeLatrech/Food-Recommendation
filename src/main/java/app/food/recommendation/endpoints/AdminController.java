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
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	Services service ;

    
	@GetMapping("/home")
	public String returnindexadmin() {
		
	    return "admin/indexadmin";
	}
	@GetMapping("/brandlist")
	public String returnbrandadmin() {
		
	    return "admin/brandlistadmin";
	}
	@GetMapping("/addbrand")
	public String returnaddbrandadmin() {
		
	    return "admin/addbrandadmin";
	}
	@GetMapping("/addcategorie")
	public String returnaddcategorieadmin() {
		
	    return "admin/addcategorieadmin";
	}
	@GetMapping("/listcategorie")
	public String returnlistcategorieadmin() {
		
	    return "admin/categorielistadmin";
	}
	@GetMapping("/userlist")
   public String AllUsers() {
		
		 return "admin/userlistadmin";
	}
	@GetMapping("/adduser")
	 public String AddUsers() {
		
		 return "admin/adduseradmin";
	}
	@GetMapping("/listreview")
	 public String Listreviews() {
		
		 return "admin/reviewlistadmin";
	}
	@GetMapping("/reportreview")
	 public String Repportreviews() {
		
		 return "admin/reportreviewadmin";
	}
	
}

