package fr.iconvoit.entity;

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
//@MappedSuperclass()
@Component
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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
	private List<People> participants = new ArrayList<People>();

	public Slot() {

	}

}
