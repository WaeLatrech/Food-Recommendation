package app.food.recommendation.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table (name="likeEntity")
public class Like {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    (name="like_id")
    private long Likeid;
	
	private long idreview;
	private long iduser;
	
	public Like(long iduser2,long idreview2) {
        this.idreview = idreview2;
        this.iduser = iduser2;
    }
    public Like() {
        
    }
}
