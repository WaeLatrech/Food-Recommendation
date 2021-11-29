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
@Table( name = "Category")
public class Category {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private int idcategory ;
    
    @Column(unique = true )
    private String categoryname ; 
    @OneToMany( mappedBy = "category")
    private  List<Recipe> Recipes;
    @OneToMany( mappedBy = "dishcategory")
    private  List<Dish> dishes;

	
}
