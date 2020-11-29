package fr.iconvoit.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.People;
import fr.iconvoit.entity.SlotTravel;

public interface SlotTravelFactory extends CrudRepository<SlotTravel, Long>{

	//TODO mettre l'id ICI
	@Query(value = "SELECT * FROM  SLOT,SLOT_TRAVEL WHERE SLOT.ID=SLOT_TRAVEL.ID AND SLOT_TRAVEL.ID NOT IN (SELECT FK_SLOT FROM SLOT_PEOPLE WHERE FK_PEOPLE= ?1 )", nativeQuery = true)
	ArrayList<SlotTravel> findTravelsOfOthers(Long long1);
}
