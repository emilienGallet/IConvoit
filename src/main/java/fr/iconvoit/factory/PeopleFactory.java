package fr.iconvoit.factory;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.People;
import fr.iconvoit.entity.Slot;

public interface PeopleFactory extends CrudRepository<People, Long>{

	//@Query("SELECT ")
	//List<Slot> findAllSlotReserved();
	People findByUsername(String username);
}
