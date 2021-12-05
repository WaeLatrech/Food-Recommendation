package app.food.recommendation.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Review {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idReview;
    private int nblike;
    private int nbdislike;
//    private float note;
    private String comment;
    @JsonIgnore
   	@ManyToOne( cascade = CascadeType.DETACH )
   	private User user;
//    @ManyToMany
//    @JoinTable(name = "LikedBy")
//    @JsonIgnore
//    private List<User> LikedBy;
//    @ManyToMany
//    @JoinTable(name = "DislikedBy")
//    @JsonIgnore
//    private List<User> dislikedBy;
    
    @ManyToOne( cascade = CascadeType.DETACH )
    private Recipe recipe;
    @Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imgReview;
    
    
    @CreationTimestamp
    private Date dateofcreation ;
	private int repport=0;

//    private float c1;
//    private float c2 ;
//    private float c3;
//    private float c4;
//    private float c5 ;

}
