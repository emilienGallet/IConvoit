package fr.iconvoit.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.sun.istack.NotNull;

@EntityScan
public class Perrsonne {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;//id for app
	private Long idSource;//if exist official sources
	private Organisation org;
	@Column(nullable = false)
	private String surname;
	@Column(nullable = false)
	private String name;

	public Perrsonne() {

	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getIdSource() {
		return idSource;
	}

	public void setIdSource(Long idSource) {
		this.idSource = idSource;
	}

	public Organisation getOrg() {
		return org;
	}

	public void setOrg(Organisation org) {
		this.org = org;
	}
}
