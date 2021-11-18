package app.food.recommendation.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table( name = "Plan")
public class Plan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idplan;
	private String planname;
	private String plandescription;
	@ManyToMany(mappedBy = "plans", cascade = CascadeType.REMOVE)
	private  List<Recipe> Recipes;
}
