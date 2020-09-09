package fr.iconvoit.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.sun.istack.NotNull;

import lombok.Data;

@EntityScan
@Table(name = "Personne")
@Data
public class People {

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
}
