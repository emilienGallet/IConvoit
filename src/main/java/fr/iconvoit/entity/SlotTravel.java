package fr.iconvoit.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

	// Emmit an error for REST
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private Long id;
	// --------

	public Localization getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(Localization startPlace) {
		this.startPlace = startPlace;
	}

	public Localization getFinishPlace() {
		return finishPlace;
	}

	public void setFinishPlace(Localization finishPlace) {
		this.finishPlace = finishPlace;
	}

	public List<Path> getPaths() {
		return paths;
	}

	public void setPaths(List<Path> paths) {
		this.paths = paths;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	Localization startPlace;
	@ManyToOne(cascade = CascadeType.ALL)
	Localization finishPlace;

	@OneToMany(cascade = CascadeType.ALL)
	List<Path> paths = new ArrayList<Path>();

	@ManyToOne(cascade = CascadeType.ALL)
	Car car;

	public SlotTravel() {

	}

	public SlotTravel(String name, String start, String end, Localization startPlace, Localization finishPlace)
			throws SlotException {
		super(name, start, end);
		this.startPlace = startPlace;
		this.finishPlace = finishPlace;
	}

	public SlotTravel(Long id, String slotName, LocalDateTime start, LocalDateTime end) {
		super(id);
		this.setSlotName(slotName);
		this.setStart(start);
		this.setEnd(end);
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
