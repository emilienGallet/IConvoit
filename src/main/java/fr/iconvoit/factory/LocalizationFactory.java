package fr.iconvoit.factory;

import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.Localization;
import fr.iconvoit.entity.Slot;

public interface LocalizationFactory extends CrudRepository<Localization, Long> {
	
}
