package fr.iconvoit.factory;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.SlotTravel;

public interface SlotTravelFactory extends CrudRepository<SlotTravel, Long> {

	// @Query(value = "SELECT * FROM SLOT,SLOT_TRAVEL WHERE SLOT.ID=SLOT_TRAVEL.ID
	// AND SLOT_TRAVEL.ID NOT IN (SELECT FK_SLOT FROM SLOT_PEOPLE WHERE FK_PEOPLE=
	// ?1 )", nativeQuery = true)
	/**
	 * 
	 * @param peopleId
	 * @author Émilien
	 * @returnList of travel (need to be parse as this object) who he doesn't participate.
	 */
	@Query(value = "SELECT SLOT.ID,SLOT_NAME,START,END, FINISH_PLACE_ID, START_PLACE_ID FROM  SLOT,SLOT_TRAVEL WHERE SLOT.ID=SLOT_TRAVEL.ID AND SLOT_TRAVEL.ID NOT IN (SELECT FK_SLOT FROM SLOT_PEOPLE WHERE FK_PEOPLE= ?1 )", nativeQuery = true)
	ArrayList<Object> findTravelsOfOthers(Long peopleId);

	@Query(value = "SELECT SLOT.ID,SLOT_NAME ,BRAND,COLOR,NB_OF_SEATS,REGISTRATION "
			+ "FROM SLOT ,SLOT_TRAVEL,SLOT_PEOPLE,CAR " + "WHERE SLOT.ID=SLOT_TRAVEL.ID "
			+ "AND SLOT_PEOPLE.FK_SLOT = SLOT.ID " + "AND CAR_ID = CAR.ID " + "AND CAR_ID = ?1 "
			+ "ORDER BY START ASC", nativeQuery = true)
	ArrayList<Object> findIdOfSlotTravelByCar(Long userID);

	/**
	 * 
	 * @param slotID
	 * @param peopleId
	 * @author Émilien
	 * @return
	 */
	@Modifying
	@Query(value ="INSERT INTO SLOT_PEOPLE VALUES (?1,?2)",nativeQuery = true)
	Integer join(Long slotID, Long peopleId);
}
