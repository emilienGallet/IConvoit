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
/**
 * 
 * @author Émilien Extended class from Slot, is focus on Slot are an travel
 *         time. The got the place who your start an travel (startPlace), and the destination place as finishPlace.
 */
public final class SlotTravel extends Slot {

	@ManyToOne(cascade = CascadeType.ALL)
	Localization startPlace;
	@ManyToOne(cascade = CascadeType.ALL)

	Localization finishPlace;

	public SlotTravel() {

	}
	public SlotTravel(Localization startPlace,Localization finishPlace) {

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
