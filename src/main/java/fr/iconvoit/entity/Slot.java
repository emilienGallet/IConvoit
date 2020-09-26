package fr.iconvoit.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
/**
 * 
 * @author Émilien
 *
 */
@MappedSuperclass()
public class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	private String str;
	@Column(nullable = false)
	private Timestamp start;
	@Column(nullable = false)
	private Timestamp end;
	//@ManyToAny(metaColumn = @Column)
	//@JoinColumn()
	//@JoinTable()
	//private List<People> participants = new ArrayList<People>();
	
	
	@Column(nullable = false)
	private String name;

	public Slot() {

	}

}
