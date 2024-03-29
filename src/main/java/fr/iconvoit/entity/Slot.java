package fr.iconvoit.entity;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.stereotype.Component;

import fr.iconvoit.IcsParser;
import fr.iconvoit.exceptions.SlotException;
import lombok.Data;



@Data
@Component
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
/**
 * 
 * @author Émilien An generic abstract class who's contain the start time and
 *         end time. Use to display planning as an List<Slot>
 */
public abstract class Slot{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSlotName() {
		return slotName;
	}

	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public List<People> getParticipants() {
		return participants;
	}

	public void setParticipants(List<People> participants) {
		this.participants = participants;
	}

	public Integer getLimitParticipate() {
		return limitParticipate;
	}

	public void setLimitParticipate(Integer limitParticipate) {
		this.limitParticipate = limitParticipate;
	}

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
	@Column(nullable = true)
	private Integer limitParticipate;
	
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

	public Slot(Long id2) {
		this.setId(id2);
	}
/*
	public boolean checkSlot(){
		LocalDateTime st ;
		LocalDateTime en;
		boolean rep=false;

		String req , res;
		req = "Select * FROM Slot" +" Where id == s.getid()"+"start == s.getstart() and ";
		res = "Select * FROM Slot" +" Where id == s.getid()"+"end == s.getend() and ";

		JdbcTemplate vJdbcTemplate = new JdbcTemplate(getDataSource());
		st=vJdbcTemplate.queryForObject(req, LocalDateTime.class,this.getId(),this.getStart());
		en = vJdbcTemplate.queryForObject(req, LocalDateTime.class,this.getId(), this.getEnd());

		if(this.start == st && this.end ==en)
			rep = true; 
		return rep;
	}
*/
}
