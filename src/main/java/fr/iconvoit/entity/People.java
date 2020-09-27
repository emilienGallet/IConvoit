package fr.iconvoit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.stereotype.Component;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
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
	private String firstname;
	@Column(nullable = false)
	private String name;

	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
	private Set<PeopleRole> roles = new HashSet<>();
	
	@ManyToMany
	private List<People> friend = new ArrayList<People>();

	@ManyToMany()
	private List<Slot> reserved = new ArrayList<Slot>();;

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

	public People(String username, String name, String firstName) {
		this.username = username;
		this.name = name;
		this.firstName = firstName;
	}
}
