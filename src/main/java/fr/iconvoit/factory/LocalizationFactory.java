package fr.iconvoit.factory;

import org.springframework.data.repository.CrudRepository;

import fr.iconvoit.entity.Localization;

public interface LocalizationFactory extends CrudRepository<Localization, Long> {
	
}
