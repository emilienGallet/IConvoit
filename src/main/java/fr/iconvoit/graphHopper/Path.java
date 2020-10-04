package fr.iconvoit.graphHopper;

import java.util.ArrayList;

import fr.iconvoit.entity.Localization;
import lombok.Data;

@Data
/**
 * 
 * @author Émilien
 * Corresponding to a way from 1 localisation to an other.
 * Each points are coordinate.
 */
public class Path {

	Localization start;
	Localization end;
	ArrayList<Localization> points;

	public Path(ArrayList<Localization> list, Localization start, Localization end) {
		this.points = list;
		this.start = start;
		this.end = end;
	}
	
}
