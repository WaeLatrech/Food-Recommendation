package app.food.recommendation.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table( name = "Brand")
public class Brand {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private int idbrand;
	private String brandname;
	private String brandDescription;
	@OneToMany( mappedBy = "brand")
	private List<Restaurant> restos;
}
