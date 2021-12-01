package app.food.recommendation.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
	private float price;
	@ManyToOne(cascade = CascadeType.DETACH )
	private DishCategory dishcategory;
	@ManyToOne(cascade = CascadeType.DETACH )
	public Restaurant resto;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imageD;
}
