package fr.iconvoit.entity;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Data
public class Localisation {
	@Id
	private String nameLocation;
	@NotNull
	private String coordinate;
	private ArrayList<Localisation> subLocalisation;
	private float longitude;
	private float latitude;

	public Localisation(){

	}

	//TODO : check if float longitude,float latitude is correct in right format 
	public Localisation(String name, float longitude,float latitude){
		this.nameLocation = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}


	public ArrayList<Localisation> getSubLocalisation() {
		return subLocalisation;
	}

	public void setSubLocalisation(ArrayList<Localisation> subLocalisation) {
		this.subLocalisation = subLocalisation;
	}

	
	public String toString(){
		return "Coordinate of"+this.getNameLocation() +
		" is "+ "this.getLongitude() ." + "this.getLatitude()";
	}
}
