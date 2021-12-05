package app.food.recommendation.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table( name = "Recipe")
public class Recipe {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private long idrecipe;
	private String ingredients;
//	@ElementCollection
//	@MapKeyColumn(name="name")
//	@Column(name="value")
//	@CollectionTable(name="ingredients")
//	private Map<String,Float> ingredients = new HashMap<String,Float>();
	private String title;
	private String description;
	private String preparationTime;
	@ManyToOne( cascade = CascadeType.DETACH )
	private User publisher;
	@ManyToOne( cascade = CascadeType.DETACH )
	private DishCategory dishcategory;
	@CreationTimestamp
	private Date dateofcreation;
	@ManyToMany
    @JoinTable(name = "Recipes")
    @JsonIgnore
	private List<Plan> plans;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imgRecipe;
	
	@OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
	private List<Review> reviews;
}
