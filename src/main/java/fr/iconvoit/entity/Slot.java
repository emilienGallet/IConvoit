package fr.iconvoit.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.springframework.jdbc.core.JdbcTemplate;
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
	private String slotName;
	@Column(nullable = true)//TODO Set to false
	private LocalDateTime start;
	@Column(nullable = true)//TODO Set to false
	private LocalDateTime end;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "slot_people",
	joinColumns = { @JoinColumn(name = "fk_slot") },
	inverseJoinColumns = { @JoinColumn(name = "fk_people") })
	private List<People> participants = new ArrayList<People>();

	public Slot() {

	}

	public boolean checkSlot(Slot s){

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

}
