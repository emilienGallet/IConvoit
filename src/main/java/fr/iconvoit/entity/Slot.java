package fr.iconvoit.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;

import lombok.Data;

@Data
/**
 * 
 * @author Émilien
 *
 */
//@MappedSuperclass()
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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
