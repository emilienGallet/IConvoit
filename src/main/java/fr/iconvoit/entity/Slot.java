package fr.iconvoit.entity;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.stereotype.Component;

import fr.iconvoit.IcsParser;
import fr.iconvoit.exceptions.SlotException;
import lombok.Data;

@Data
/**
 * 
 * @author Émilien
 *
 */

@Component
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
/**
 * 
 * @author emilien An generic abstract class who's contain the start time and
 *         end time. Use to display planning as an List<Slot>
 */
public abstract class Slot {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Column(nullable = false)
	/**
	 * Name of this slot
	 */
	private String slotName;
	@Column(nullable = false)
	/**
	 * Start of this slot
	 */
	private LocalDateTime start;
	@Column(nullable = false)
	/**
	 * end of this slot
	 */
	private LocalDateTime end;
	@Column(nullable = true)
	/**
	 * If the slot come from an other calendar. By default is null.
	 */
	private URL url;
	@Column(nullable = true)
	/**
	 * If URL column is set. uid permit to know his id on ADE DB or Other.
	 */
	private String uid;
	@Column(nullable = true)
	/**
	 * If URL column is set. lastModified permit to know if is update or not.
	 */
	private LocalDateTime lastModified;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "slot_people", joinColumns = { @JoinColumn(name = "fk_slot") }, inverseJoinColumns = {
			@JoinColumn(name = "fk_people") })
	private List<People> participants = new ArrayList<People>();

	
	public Slot() {

	}

	public Slot(String name, String start2, String end2) throws SlotException {
		this.slotName = name;
		try {
			this.start = IcsParser.dtTimeToLocalDateTime(start2);
		} catch (DateTimeParseException e) {
			new SlotException();
		}
		try {
			this.end = IcsParser.dtTimeToLocalDateTime(end2);
		} catch (DateTimeParseException e) {
			new SlotException();
		}

	}

}
