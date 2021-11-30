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
@Table( name = "Brand")
public class Brand {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private long idbrand;
	private String brandname;
	private String brandDescription;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imageB;
	@OneToMany( mappedBy = "brand")
	private List<Restaurant> restos;
}
