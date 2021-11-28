package app.food.recommendation.endpoints;



import java.io.IOException;
import java.sql.Date;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import app.food.recommendation.models.User;
import app.food.recommendation.services.ServiceImp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Controller
@Data
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	ServiceImp service ;

	public String CheckRole () {
		Collection<? extends GrantedAuthority> authorities;
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    authorities = auth.getAuthorities();
	     
		return authorities.toArray()[0].toString();
	}
    
	@GetMapping("/home")
	public String returnindexadmin() {
		String myRole = CheckRole();
	    if (myRole.equals("USER")||myRole.equals("NOTVERIFIED")) {
	        return "redirect:/user/home";
	    }
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
	
	/***********Users************/
	
	@GetMapping("/userlist")
   public String AllUsers(Model model) {
		List<User> users =  service.getAllUsers();
		model.addAttribute("users",users);
		User user = new User();
		model.addAttribute("user",user);
		 return "admin/userlistadmin";
	}
	
	@GetMapping("/adduser")
	 public String AddUsers(Model model) {
		User user = new User();
		model.addAttribute("user",user);
		return "admin/adduseradmin";
	}
	
	@PostMapping("/adduser")
	public String UserregisterSuccess(@ModelAttribute("user") User user, Model model,@RequestParam("file")MultipartFile file ) {
		
		String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	try {
			user.setImageU(Base64.getEncoder().encodeToString(file.getBytes()));
			System.out.println("cv");
		} catch (IOException e) {
			System.out.println("dowiw");
			e.printStackTrace();
		}
	
		service.createUser(user);
		
		return "redirect:/admin/userlist";
	}
	
	@GetMapping("/deluser/{id}")
	public String DelUsers(@PathVariable("id") Long id, Model model) {
	    service.DelTokenByIdUser(id);
		service.deleteUser(id);
		return "redirect:/admin/userlist";
	}
	
	@GetMapping("/upduser/{id}")
	public String UpdUsers(@PathVariable("id") int id, Model model) {
		User user = service.getUserById(id);
		model.addAttribute("user", user);
	    return "admin/upduseradmin";
	}
	@PostMapping("/upduseradmin/{id}")
	public String EditSuucesUser( Model model ,@PathVariable("id") long id ,@RequestParam ("username") String username , @RequestParam ("email") String email , @RequestParam("password") String password, @RequestParam("phone") String phone,@RequestParam ("birthDate") @DateTimeFormat(pattern="yyyy-MM-dd") String birthDate,@RequestParam("Role") String Role , @RequestParam("file") MultipartFile file  ) {
		 User user =new User();
		 user.setUsername(username);
		 user.setEmail(email);
		 user.setPassword(password);
		 user.setPhone(phone);
		 user.setBirthDate(birthDate);
		 user.setRole(Role);
		 String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
	    	if(FileName.contains("..")) {
	    		System.out.println("not a proper file ");
	    	}
	    	try {
	    		if(!FileName.isEmpty())
	    			user.setImageU(Base64.getEncoder().encodeToString(file.getBytes()));
				else 
					user.setImageU(service.getUserById(id).getImageU());
				
				System.out.println("cv");
			} catch (IOException e) {
				System.out.println("dowiw");
				e.printStackTrace();
			}
		
		 
		 service.modifyUser(id, user);
		 return "redirect:/admin/userlist"; 
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

