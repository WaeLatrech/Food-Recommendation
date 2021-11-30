package app.food.recommendation.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table( name = "placeCategory")
public class Category {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private long idcategory ;

    @Column(unique = true )
    private String placecategory ; 
    @OneToMany( mappedBy = "placecategory")
    private  List<Restaurant> restos;


	
}
