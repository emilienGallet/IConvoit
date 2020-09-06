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
}
