package app.food.recommendation.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String logo;
   
	@OneToMany( mappedBy = "resto")
	private List<Dish> menu; 
	
}
