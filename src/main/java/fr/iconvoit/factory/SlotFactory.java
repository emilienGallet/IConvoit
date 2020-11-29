package fr.iconvoit.factory;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.People;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.entity.SlotTravel;

public interface SlotFactory extends CrudRepository<Slot, Long> {
	//SELECT * FROM PEOPLE WHERE PEOPLE.ID IN (SELECT FK_PEOPLE  FROM SLOT_PEOPLE WHERE FK_SLOT = ?1 )
	@Query(value = "SELECT FK_PEOPLE  FROM SLOT_PEOPLE WHERE FK_SLOT = ?1 ", nativeQuery = true)
	ArrayList<Long> findParticipant(Long long1);
	
}
