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
import javax.persistence.OneToMany;

import com.sun.istack.NotNull;

import fr.iconvoit.graphHopper.Path;

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
	private List<Path> ways;

	@ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
	private Set<PeopleRole> roles = new HashSet<>();
	
	@ManyToMany
	private List<People> friend = new ArrayList<People>();

	@ManyToMany(mappedBy = "participants")
	private List<Slot> reserved = new ArrayList<Slot>();

	@OneToMany(mappedBy = "owner")
	private List<Car> myCars = new ArrayList<Car>();

	public People() {

	}

	public People(String username, String name, String firstName) {
		this.username = username;
		this.name = name;
		this.firstname = firstName;
	//	this.cars = new ArrayList<Car>();

	}
	
	public List<Slot> getSlotTravel(){
		List<Slot> allSlotTravel = new ArrayList<Slot>();
		for (Slot slot : reserved) {
			if (slot.getClass() == SlotTravel.class) {
				allSlotTravel.add(slot);
			}
		}
		return allSlotTravel;
	}
	public List<Slot> getSlotOthers(){
		List<Slot> allSlotOthers= new ArrayList<Slot>();
		for (Slot slot : reserved) {
			if (slot.getClass() == SlotTravel.class) {
				allSlotOthers.add(slot);
			}
		}
		return allSlotOthers;
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
		this.myCars.add(c);
	}
	public void addCar(Car c){
		this.myCars.add(c);
	}
	public void removeCar(int index){
	//	this.cars.remove(index);
	}
}
