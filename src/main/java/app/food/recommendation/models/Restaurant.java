package app.food.recommendation.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table( name = "Restaurant")
public class Restaurant {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private int idresto;
	private String restoname;
	private String location;
	@ManyToOne( cascade = CascadeType.DETACH )
	private Category placecategory;
	@OneToMany( mappedBy = "resto")
	private List<Dish> menu; 
	
	@ManyToOne( cascade = CascadeType.DETACH )
	private Brand brand;
	private String brandname;
}
