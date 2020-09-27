package fr.iconvoit.factory;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.Slot;


public interface SlotFactory extends CrudRepository<Slot, Long> {

}
