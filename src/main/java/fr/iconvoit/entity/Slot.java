package fr.iconvoit.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.Id;

import lombok.Data;


@Data
/**
 * 
 * @author Émilien
 *
 */
/* NEED TO BE APPLY SOON WITHOUT ERROR*/
//@MappedSuperclass
/**************************************/
public class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	private String str;
	@Column(nullable = false)
	private Timestamp start;
	@Column(nullable = false)
	private Timestamp end;
	@ManyToMany()
	private PeopleList<People> participants;
	@Column(nullable = false)
	private String name;
	
	Slot(){
		
	}
	
}
