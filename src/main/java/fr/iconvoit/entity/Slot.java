package fr.iconvoit.entity;

import java.sql.Timestamp;
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
//@MappedSuperclass()
@Component
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	private String str;
	@Column(nullable = false)
	private Timestamp start;
	@Column(nullable = false)
	private Timestamp end;
	@ManyToMany
	private List<People> participants = new ArrayList<People>();
	
	
	@Column(nullable = false)
	private String name;

	public Slot() {

	}

}
