package app.food.recommendation.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table( name = "Dish")
public class Dish {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long iddish;
	private String dishname;
	private String dishdescription;
	private String price;
	@ManyToOne(cascade = CascadeType.DETACH )
	private DishCategory dishcategory;
	@ManyToOne(cascade = CascadeType.DETACH )
	public Restaurant resto;
}
