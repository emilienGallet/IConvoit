package fr.iconvoit.factory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.People;

public interface PeopleFactory extends CrudRepository<People, Long>{

	//List<Slot> findAllSlotReserved();

	People findByUsername(String username);

	
}
