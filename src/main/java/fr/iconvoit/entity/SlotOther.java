package fr.iconvoit.entity;

import javax.persistence.Entity;
import org.springframework.stereotype.Component;

import fr.iconvoit.IcsParser;
import fr.iconvoit.exceptions.SlotException;

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
	Localization place;

	public SlotOther() {

	}

	/**
	 * 
	 * @param name
	 * @param start
	 * @param end
	 * @param location
	 * @param uid
	 * @throws SlotException
	 */
	public SlotOther(String name, String start, String end, String location, String uid) throws SlotException {
		super(name, start, end);
		this.place = IcsParser.AdeParsing(location);
		if (this.place == null) {
			new SlotException();
		}
		this.setUid(uid);
	}
}
