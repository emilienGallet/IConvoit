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
}
