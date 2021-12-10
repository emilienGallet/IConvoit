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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Localization> getPoints() {
		return points;
	}

	public void setPoints(List<Localization> points) {
		this.points = points;
	}

	public Integer getTrajectTime() {
		return trajectTime;
	}

	public void setTrajectTime(Integer trajectTime) {
		this.trajectTime = trajectTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = true)
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Localization> points;
	
	private Integer trajectTime;

	public Path() {
		
	}
	
	public Path(ArrayList<Localization> jsonArrayToList, Integer time) {
		this.points = jsonArrayToList;
		this.trajectTime = time;
	}

}
