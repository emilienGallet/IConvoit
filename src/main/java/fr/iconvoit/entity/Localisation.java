package fr.iconvoit.entity;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Data
/**
 * 
 * @author Chrithian, reviewed by emilien
 * @version 0.2
 */
public class Localisation {
	@Id
	private String nameLocation;
	@Column(nullable = false)
	private ArrayList<Localisation> subLocalisation;
	private Double longitude;
	private Double latitude;

	public Localisation(String nameLocation, double longitude, double latitude) {
		super();
		this.nameLocation = nameLocation;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public ArrayList<Localisation> getSubLocalisation() {
		return subLocalisation;
	}

	public void setSubLocalisation(final ArrayList<Localisation> subLocalisation) {
		this.subLocalisation = subLocalisation;
	}

	public String getGeolocalization() {
		return this.getLatitude().toString()+this.getLongitude().toString();
	}
}
