package fr.iconvoit.entity;



import org.springframework.data.repository.CrudRepository;

public interface PeopleList extends CrudRepository<People,Long> {


	People findByUsername(String username);
	

}
