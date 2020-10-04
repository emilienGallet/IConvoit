package fr.iconvoit.entity;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import lombok.Data;

@Entity
@Data
/**
 * 
 * @author Chrithian, reviewed by emilien
 * @version 0.2
 */
@Component
public class Localization {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	@Column(nullable = false)
	private String nameLocation;
	@Column(nullable = true)
	private ArrayList<Localization> subLocalization;
	private Double longitude;
	private Double latitude;

	
	
	/**
	 * Build an localization
	 * @param nameLocation
	 * @param latitude
	 * @param longitude
	 */
	public Localization(String nameLocation, double latitude,double longitude) {
		super();
		if (!nameLocation.isEmpty()) {
			this.nameLocation = nameLocation;	
		}else {
			this.nameLocation = String.valueOf(latitude)+"|"+String.valueOf(longitude);
		}
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Localization() {
		super();
	}

	public ArrayList<Localization> getSubLocalization() {
		return subLocalization;
	}

	public void setSubLocalization(final ArrayList<Localization> subLocalization) {
		this.subLocalization = subLocalization;
	}

	public String getGeolocalization() {
		return this.getLatitude().toString()+this.getLongitude().toString();
	}

}
