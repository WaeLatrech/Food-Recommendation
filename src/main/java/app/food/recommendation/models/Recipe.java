package app.food.recommendation.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table( name = "Recipe")
public class Recipe {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private int idrecipe;
	private String[] ingredients;
	private String description;
	private String preparationTime;
	@ManyToOne( cascade = CascadeType.DETACH )
	private User publisher;
	@ManyToOne( cascade = CascadeType.DETACH )
	private Category category;
	@ManyToMany
    @JoinTable(name = "Recipes")
    @JsonIgnore
	private List<Plan> plans;
}