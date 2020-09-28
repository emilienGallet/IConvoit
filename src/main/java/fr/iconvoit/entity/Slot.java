package fr.iconvoit.entity;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.springframework.stereotype.Component;

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
 * @author emilien
 * An generic abstract class who's contain the start time and end time.
 * Use to display planning as an List<Slot>
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
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "slot_people",
	joinColumns = { @JoinColumn(name = "fk_slot") },
	inverseJoinColumns = { @JoinColumn(name = "fk_people") })
	private List<People> participants = new ArrayList<People>();

	public Slot() {

	}

}
