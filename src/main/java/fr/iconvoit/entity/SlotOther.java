package fr.iconvoit.entity;

import javax.persistence.Entity;

import org.springframework.stereotype.Component;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Component
/**
 * 
 * @author Émilien Extended class from Slot, is focus on Slot won't be an travel
 *         time. Is more focus for Unique place.
 */
public class SlotOther extends Slot {

	@ManyToOne(cascade = CascadeType.ALL)
	Localisation place;

	public SlotOther() {

	}
}
