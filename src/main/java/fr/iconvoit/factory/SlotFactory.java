package fr.iconvoit.factory;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.People;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.entity.SlotTravel;

public interface SlotFactory extends CrudRepository<Slot, Long> {

}
