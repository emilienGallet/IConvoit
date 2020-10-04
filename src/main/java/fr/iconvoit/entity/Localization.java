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
public class Localization {
	@Id
	private String nameLocation;
	@Column(nullable = false)
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
		this.nameLocation = nameLocation;
		this.longitude = longitude;
		this.latitude = latitude;
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
