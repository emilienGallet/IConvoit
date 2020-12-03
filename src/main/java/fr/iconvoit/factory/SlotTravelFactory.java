package fr.iconvoit.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.Car;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.SlotTravel;

public interface SlotTravelFactory extends CrudRepository<SlotTravel, Long> {

	// @Query(value = "SELECT * FROM SLOT,SLOT_TRAVEL WHERE SLOT.ID=SLOT_TRAVEL.ID
	// AND SLOT_TRAVEL.ID NOT IN (SELECT FK_SLOT FROM SLOT_PEOPLE WHERE FK_PEOPLE=
	// ?1 )", nativeQuery = true)
	@Query(value = "SELECT SLOT.ID,SLOT_NAME,START,END, FINISH_PLACE_ID, START_PLACE_ID FROM  SLOT,SLOT_TRAVEL WHERE SLOT.ID=SLOT_TRAVEL.ID AND SLOT_TRAVEL.ID NOT IN (SELECT FK_SLOT FROM SLOT_PEOPLE WHERE FK_PEOPLE= ?1 )", nativeQuery = true)
	ArrayList<Object> findTravelsOfOthers(Long long1);

	@Query(value = "SELECT SLOT.ID,SLOT_NAME ,BRAND,COLOR,NB_OF_SEATS,REGISTRATION "
			+ "FROM SLOT ,SLOT_TRAVEL,SLOT_PEOPLE,CAR " + "WHERE SLOT.ID=SLOT_TRAVEL.ID "
			+ "AND SLOT_PEOPLE.FK_SLOT = SLOT.ID " + "AND CAR_ID = CAR.ID " + "AND CAR_ID = ?1 "
			+ "ORDER BY START ASC", nativeQuery = true)
	ArrayList<Object> findIdOfSlotTravelByCar(Long userID);

}
