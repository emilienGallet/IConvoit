package fr.iconvoit.entity;

import javax.persistence.Entity;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
/**
 * 
 * @author Émilien Extended class from Slot, is focus on Slot won't be an travel
 *         time. Is more focus for Unique place.
 */
public class SlotOther extends Slot {

	@ManyToOne
	Localisation place;

	public SlotOther() {

	}
}
