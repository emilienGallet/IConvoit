package fr.iconvoit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.stereotype.Component;

import com.sun.istack.NotNull;

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
	public ArrayList<Car> cars;

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

	public void addListSlot(ArrayList<Slot> sl) {
		this.reserved.addAll(sl);
		for (Slot slot : sl) {
			slot.getParticipants().add(this);
		}
	public Long getIdSource() {
		return idSource;
	}

	public void setIdSource(Long idSource) {
		this.idSource = idSource;
	}

	public Organisation getOrg() {
		return org;
	}

	public void setOrg(Organisation org) {
		this.org = org;
	}

	public ArrayList<Car> getCar() {
		/*for(int i=0;i < cars.size();i++){
			System.out.println(cars.get(i));
		}*/
		return cars;
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
		cars.add(c);
	}

	public void removeCar(int index){
		cars.remove(index);
	}
}
