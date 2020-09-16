package fr.iconvoit.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;



/**
 * 
 * @author Émilien
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public final class SlotTravel extends Slot {
	@OneToOne
	Localisation  startPlace;
	@OneToOne
	Localisation  finishPlace;
	
	SlotTravel(){
		
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
