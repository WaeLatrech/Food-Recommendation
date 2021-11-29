package app.food.recommendation.models;

import java.util.Date;
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

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;


@Data	
@Entity	
@Table (name="UserEntity")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password; 
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthDate;
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
