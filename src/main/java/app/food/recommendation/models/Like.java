package app.food.recommendation.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Like {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    (name="like_id")
    private long Likeid;
	
	private int idreview;
	private int iduser;
	
	public Like(int iduser,int idreview) {
        this.idreview = idreview;
        this.iduser = iduser;
    }
    public Like() {
        
    }
}
