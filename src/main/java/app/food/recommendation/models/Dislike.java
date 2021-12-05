package app.food.recommendation.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Dislike {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    (name="Dislikeid")
    private long Dislikeid;
	
	private int idreview;
	private int iduser;
	
	public Dislike(int iduser,int idreview) {
        this.idreview = idreview;
        this.iduser = iduser;
    }
    public Dislike() {
        
    }
}
