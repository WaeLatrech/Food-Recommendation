package app.food.recommendation.endpoints;



import java.io.IOException;
import java.sql.Date;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.food.recommendation.models.Brand;
import app.food.recommendation.models.Category;
import app.food.recommendation.models.Dish;
import app.food.recommendation.models.DishCategory;
import app.food.recommendation.models.Recipe;
import app.food.recommendation.models.Restaurant;
import app.food.recommendation.models.User;
import app.food.recommendation.repositories.CategoryRepo;
import app.food.recommendation.repositories.DishCategoryRepo;
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

	private CategoryRepo catRepo;
	private DishCategoryRepo catDishRepo;
	
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
	
	/***********Brands************/
	
	@GetMapping("/brandlist")
	public String returnbrandadmin(Model model) {
		List<Brand> brands =  service.getAllBrands();
		model.addAttribute("brands",brands);
		Brand brand = new Brand();
		model.addAttribute("brand",brand);
	    return "admin/brandlistadmin";
	}
	@GetMapping("/addbrand")
	public String returnaddbrandadmin(Model model) {
		Brand brand = new Brand();
		model.addAttribute("brand",brand);
	    return "admin/addbrandadmin";
	}
	
	@PostMapping("/addbrand")
	public String registerSuccessbrand(@ModelAttribute("brand") Brand brand, Model model,@RequestParam("file")MultipartFile file) {
		String FileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    	if(FileName.contains("..")) {
    		System.out.println("not a proper file ");
    	}
    	try {
			brand.setImageB(Base64.getEncoder().encodeToString(file.getBytes()));
			System.out.println("cv");
		} catch (IOException e) {
			System.out.println("dowiw");
			e.printStackTrace();
		}
		
		service.createBrand(brand);
		
		return  "redirect:/admin/brandlist";
	}
	
	@GetMapping("/delbrand/{id}")
	public String DeleteBrand(@PathVariable("id") int id, Model model) {	    
		System.out.println("$$$$$$$$$$$$$$");
		service.deleteBrand(id);
		System.out.println("$$$$$$$$$$$$$$");
		return "redirect:/admin/brandlist";
	}
	

	/***********Dish************/
	
	@GetMapping("/dishlist")
	public String listdisheadmin(Model model) {
		List<Dish> dishes =  service.getAllDishes();
		model.addAttribute("dishes",dishes);
		Dish dishe = new Dish();
		model.addAttribute("dishe",dishe);
	    return "admin/dishlistadmin";
	}
							
	@GetMapping("/adddish")
	public String adddisheadmin(Model model) {
		Dish dish = new Dish();
		model.addAttribute("dish",dish);
		List<Restaurant> restos =  service.getAllRestos();
		model.addAttribute("restos",restos);
		Restaurant resto = new Restaurant();
		model.addAttribute("resto",resto);
		List<DishCategory> dishcats =  service.getAllDishCategories();
		model.addAttribute("dishcats",dishcats);
		DishCategory dishcat = new DishCategory();
		model.addAttribute("dishcat",dishcat);
	    return "admin/adddishadmin";
	}
	
	@PostMapping("/adddish")
	public String registerSuccessdish(RedirectAttributes redirAttrs,Model model, @RequestParam ("dishcatname") String dishcatname ,  @RequestParam ("restoname") String restoname ,@RequestParam ("dishname") String dishname, 
			@RequestParam ("dishdescription") String dishdescription,@RequestParam ("price") String price, @RequestParam ("file") MultipartFile file) {
		
		service.createDish(restoname,dishcatname,dishname,dishdescription,Float.parseFloat(price),file);
		redirAttrs.addFlashAttribute("success", "Dish Created Successfully");
		return  "redirect:/admin/dishlist";
	}
	/***********Place Categories************/
	
	@GetMapping("/addcategorie")
	public String addcategorieadmin(Model model) {
		Category Category = new Category();
		model.addAttribute("category",Category);
	    return "admin/addcategorieadmin";
	}
	@PostMapping("/addcategorie")
	public String addcategorieadmin(Model model, @RequestParam ("placecategory") String catname,
			RedirectAttributes redirAttrs) {
		if (catRepo.findByPlacecategory(catname)!= null)
		{	
        	redirAttrs.addFlashAttribute("error", "Category Name already exists");
        	return "redirect:/admin/addcategorie";
        }
		Category Category = new Category();
		Category.setPlacecategory(catname);
		service.createCategory(Category);
		redirAttrs.addFlashAttribute("success", "Category Created Successfully");
		return  "redirect:/admin/listcategorie";
	}
	@GetMapping("/listcategorie")
	public String returnlistcategorieadmin(Model model) {
		
		List <Category> categories = catRepo.findAll();
		model.addAttribute("categories",categories);
	    return "admin/categorielistadmin";
	}
	//updcategorie
	@GetMapping("/delcategorie/{id}")
	public String DelCat(RedirectAttributes redirAttrs,@PathVariable("id") Long id, Model model) {
		//add all dishes in this category and delete them
		Category a = service.getCategoryById(id);
		for (Restaurant resto : service.getCategoryById(id).getRestos()) {
			service.deleteResto(resto.getIdresto());
		}

		service.deleteCategory(id);
        redirAttrs.addFlashAttribute("success", "Category deleted");
		return "redirect:/admin/listcategorie";
	}
	/***********Dish Categories************/
	@GetMapping("/adddishcategorie")
	public String adddishcategorieadmin(Model model) {
		DishCategory DishCategory = new DishCategory();
		model.addAttribute("DishCategory",DishCategory);
	    return "admin/dishaddcategorieadmin";
	}
	@PostMapping("/adddishcategorie")
	public String adddishcategorieadmin(Model model, @RequestParam ("dishcategory") String catname,
			RedirectAttributes redirAttrs) {
		if (catDishRepo.findByDishcategory(catname)!= null)
		{	
        	redirAttrs.addFlashAttribute("error", "Category Name already exists");
        	return "redirect:/admin/adddishcategorie";
        }
		DishCategory Category = new DishCategory();
		Category.setDishcategory(catname);
		service.createDishCategory(Category);
		redirAttrs.addFlashAttribute("success", "Category Created Successfully");
		return  "redirect:/admin/listdishcategorie";
	}
	@GetMapping("/listdishcategorie")
	public String listdishcategorieadmin(Model model) {
		List <DishCategory> categories = catDishRepo.findAll();
		model.addAttribute("categories",categories);
	    return "admin/dishcategorielistadmin";
	}

	@GetMapping("/deldishcategorie/{id}")
	public String DelDishCat(RedirectAttributes redirAttrs,@PathVariable("id") Long id, Model model) {
		//add all dishes in this category and delete them
		DishCategory a = service.getDishCategoryById(id);
		for (Dish dish : service.getDishCategoryById(id).getDishes()) {
			service.deleteDish(dish.getIddish());
		}
		for (Recipe recipe : service.getDishCategoryById(id).getRecipes()) {
			service.deleteRecipe(recipe.getIdrecipe());
		}
		service.deleteDishCategory(id);
        redirAttrs.addFlashAttribute("success", "Category deleted");
		return "redirect:/admin/listdishcategorie";
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
	public String EditSuucesUser( Model model ,@PathVariable("id") long id ,@RequestParam ("username") String username , @RequestParam ("email") String email , @RequestParam("password") String password, @RequestParam("phone") String phone,@RequestParam ("birthDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date birthDate,@RequestParam("Role") String Role , @RequestParam("file") MultipartFile file  ) {
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
	
	/***********Restos************/
	
	@GetMapping("/restolist")
	public String listrestoadmin(Model model) {
		List<Restaurant> restos =  service.getAllRestos();
		model.addAttribute("restos",restos);
		Restaurant resto = new Restaurant();
		model.addAttribute("resto",resto);
	    return "admin/restolistadmin";
	}
	
	@GetMapping("/addresto")
	public String addrestoadmin(Model model) {
		Restaurant resto = new Restaurant();
		model.addAttribute("resto",resto);
		List<Brand> brands =  service.getAllBrands();
		model.addAttribute("brands",brands);
		Brand brand = new Brand();
		model.addAttribute("brand",brand);
	    return "admin/addrestoadmin";
	}
	
	@PostMapping("/addresto")
	public String registerSuccessresto(RedirectAttributes redirAttrs,Model model, @RequestParam ("brandname") String brandname , @RequestParam ("restoname") String restoname, @RequestParam ("location") String location) {
		service.createResto(brandname,restoname,location);
		redirAttrs.addFlashAttribute("success", "Resto Created Successfully");
		return  "redirect:/admin/restolist";
	}
	
	//updresto
	
	@GetMapping("/delresto/{id}")
	public String DeleteResto(@PathVariable("id") int id, Model model) {	    
		System.out.println("$$$$$$$$$$$$$$");
		service.deleteResto(id);
		System.out.println("$$$$$$$$$$$$$$");
		return "redirect:/admin/restolist";
	}
	
	/***********Review************/
	
	@GetMapping("/listreview")
	 public String Listreviews() {
		 
		 return "admin/reviewlistadmin";
	}
	@GetMapping("/reportreview")
	 public String Repportreviews() {
		
		 return "admin/reportreviewadmin";
	}
	
}

