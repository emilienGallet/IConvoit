package fr.iconvoit.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.stereotype.Component;

import fr.iconvoit.exceptions.SlotException;
import fr.iconvoit.graphHopper.Path;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Component
/**
 * 
 * @author Émilien Extended class from Slot, is focus on Slot are an travel
 *         time. The got the place who your start an travel (startPlace), and
 *         the destination place as finishPlace.
 */
public final class SlotTravel extends Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	Localization startPlace;
	@ManyToOne(cascade = CascadeType.ALL)
	Localization finishPlace;

	@OneToMany(cascade = CascadeType.ALL)
	List<Path> paths = new ArrayList<Path>();

	public SlotTravel() {

	}

	public SlotTravel(String name, String start, String end, Localization startPlace, Localization finishPlace)
			throws SlotException {
		super(name, start, end);
		this.startPlace = startPlace;
		this.finishPlace = finishPlace;
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
