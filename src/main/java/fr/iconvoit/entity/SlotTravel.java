package fr.iconvoit.entity;

import javax.persistence.*;

import org.springframework.stereotype.Component;

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
@Component
public final class SlotTravel extends Slot {

	@ManyToOne(cascade = CascadeType.ALL)
	Localisation startPlace;
	@ManyToOne(cascade = CascadeType.ALL)
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
