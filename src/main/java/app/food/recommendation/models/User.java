package app.food.recommendation.models;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
@Table (name="UserEntity")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String firstName;
	private String lastName;
	private String username;
	private String password; //prof qal mayet7atech el password fel table houni
	private String birthDate;
	private String gender;
	private String email;
	private String phone;
	private String Role;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imageU;
    private int verified=0;
    
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.REMOVE)
	private List<Recipe> recipes;
	
}
