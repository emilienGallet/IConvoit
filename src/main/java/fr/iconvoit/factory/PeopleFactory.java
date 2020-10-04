package fr.iconvoit.factory;

import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.People;

public interface PeopleFactory extends CrudRepository<People, Long>{

	//@Query("SELECT ")
	//List<Slot> findAllSlotReserved();
}
