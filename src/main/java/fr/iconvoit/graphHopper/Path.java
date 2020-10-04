package fr.iconvoit.graphHopper;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.stereotype.Component;

import fr.iconvoit.entity.Localization;
import lombok.Data;

@Data
/**
 * 
 * @author Émilien Corresponding to a way from 1 localisation to an other. Each
 *         points are coordinate.
 */
@Entity
@Component
public class Path {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = true)
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Localization> points;

	public Path() {
		
	}
	
	public Path(ArrayList<Localization> jsonArrayToList) {
		this.points = jsonArrayToList;
	}

}
