package fr.iconvoit.entity;

import java.util.ArrayList;

import javax.persistence.Id;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.sun.istack.NotNull;

@EntityScan
public class Localisation {
	@Id
	@NotNull
	private String nameLocation;
	private ArrayList<Localisation> subLocalisation;
	private float longitude;
	private float latitude;

	public Localisation(){

	}

	public Localisation(String name, float longitude,float latitude){
		this.nameLocation = name;
		if(longitude == 0 && latitude == 0)
			System.err("Erreur de Syntaxe");
		this.longitude = longitude;
		this.latitude = latitude;
	}


	public ArrayList<Localisation> getSubLocalisation() {
		return subLocalisation;
	}

	public void setSubLocalisation(ArrayList<Localisation> subLocalisation) {
		this.subLocalisation = subLocalisation;
	}

	public String getNameLocation() {
		return nameLocation;
	}

	public void setNameLocation(String nameLocation) {
		this.nameLocation = nameLocation;
	}

	
	public String toString(){
		return "Coordinate of"+this.getNameLocation() +
		" is "+ "this.getLongitude() ." + "this.getLatitude()";
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
}
