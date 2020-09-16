package fr.iconvoit.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
/**
 * 
 * @author Émilien
 * Extended class from Slot, is focus on Slot won't be an travel time. Is more focus for Unique place.
 */
public class SlotOther extends Slot{
	
	/* WILL BE DEPRECATED SOON */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	/***************************/
	@OneToOne
	Localisation place;
	
	SlotOther(){
		
	}
}
