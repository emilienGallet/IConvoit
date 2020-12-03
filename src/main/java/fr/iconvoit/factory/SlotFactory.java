package fr.iconvoit.factory;

import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.Slot;

public interface SlotFactory extends CrudRepository<Slot, Long> {
 
}
