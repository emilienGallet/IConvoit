package fr.iconvoit.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Émilien
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public final class SlotTravel extends Slot {

	@ManyToOne
	Localisation startPlace;
	@ManyToOne
	Localisation finishPlace;

	public SlotTravel() {

	}

	/**
	 * 
	 * @return
	 */
	public boolean confimTravel() {
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean eraseTravel() {
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean createTravel() {
		return false;
	}

	/**
	 * 
	 */
	public void alertTravelErase() {

	}

}
