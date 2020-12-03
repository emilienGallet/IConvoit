package fr.iconvoit.factory;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.People;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.entity.SlotTravel;

public interface SlotFactory extends CrudRepository<Slot, Long> {
	//SELECT FK_PEOPLE  FROM SLOT_PEOPLE WHERE FK_SLOT = ?1
	//SELECT * FROM PEOPLE WHERE PEOPLE.ID IN (SELECT FK_PEOPLE  FROM SLOT_PEOPLE WHERE FK_SLOT = ?1 )
	@Query(value = "SELECT ID,USERNAME,FIRSTNAME,NAME FROM PEOPLE WHERE PEOPLE.ID IN (SELECT FK_PEOPLE  FROM SLOT_PEOPLE WHERE FK_SLOT = ?1 ) ", nativeQuery = true)
	ArrayList<Object> findParticipant(Long long1);
	//Failed to convert from type [java.lang.Object[]] to type [fr.iconvoit.entity.People] for value '{2, a, a, a, null, null}';
	//In spite of the creation of a specific builder and the conversion of a BigInteger in Long
	//ArrayList<Long> findParticipant(Long long1);
	

	/*@Query(value = "SELECT COUNT(*) AS bool "
			+ "FROM SLOT,SLOT_PEOPLE  "
			+ "WHERE SLOT.ID=FK_SLOT "
			+ "AND FK_SLOT = ?1 "
			+ "AND LIMIT_PARTICIPATE > "
				+ "(SELECT COUNT(FK_PEOPLE) AS PARTICIPATE "
				+ "FROM SLOT,SLOT_PEOPLE  "
				+ "WHERE SLOT.ID=FK_SLOT "
				+ "AND FK_SLOT = ?1)",nativeQuery = true)
	Boolean joinSlot(Long s,Long user);*/
}
