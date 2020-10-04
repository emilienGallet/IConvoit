package fr.iconvoit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sun.istack.NotNull;

import org.springframework.stereotype.Component;

import lombok.Data;

@Entity
@Data
@Component
/**
 * 
 * @author Émilien
 * Managed People entity 
 */
public class People {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;// id for app
	private Long idSource;// if exist official sources
	private Organisation org;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String firstname;
	@Column(nullable = false)
	private String name;

	private String password;

	@OneToMany
	private List<Car> cars;

	@ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
	private Set<PeopleRole> roles = new HashSet<>();
	
	@ManyToMany
	private List<People> friend = new ArrayList<People>();

	@ManyToMany(mappedBy = "participants")
	private List<Slot> reserved = new ArrayList<Slot>();

	public People() {

	}

	public People(String username, String name, String firstName) {
		this.username = username;
		this.name = name;
		this.firstname = firstName;
		this.cars = new ArrayList<Car>();

	}


/**
		 * @author melanie
         * add a car to the person repository
         * @param String color,
		 * @param String brand,
		 * @param String registration,
		 * @param String Format,
		 * @param Integer nbOfSeats
         * @return boolean
         */
	public void addCar(String color,String brand,String registration,String Format,Integer nbOfSeats){
		Car c = new Car(color,brand,registration,Format,nbOfSeats);
		this.cars.add(c);
	}

	public void removeCar(int index){
		this.cars.remove(index);
	}
}
