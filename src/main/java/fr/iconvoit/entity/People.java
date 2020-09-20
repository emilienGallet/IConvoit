package fr.iconvoit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import org.springframework.stereotype.Component;

import lombok.Data;

@Entity
@Table(name = "Personne")
@Data
@Component
public class People {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;// id for app
	private Long idSource;// if exist official sources
	private Organisation org;

	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String fistName;

	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
	private Set<PeopleRole> roles = new HashSet<>();


	public People() {

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

	public People(String username, String name, String fistName) {
		this.username = username;
		this.name = name;
		this.fistName = fistName;
	}
}
