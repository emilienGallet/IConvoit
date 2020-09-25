package fr.iconvoit.factory;

import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.Slot;


public interface SlotFactory/*<Slot, Long>*/ extends CrudRepository<Slot, Long> {

}
