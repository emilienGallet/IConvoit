package fr.iconvoit.entity;

import java.security.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

//@EntityScan
public class Creneau {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private Timestamp debut;
	@Column(nullable = false)
	private Timestamp fin;
	@ManyToMany()
	private ListePersonnes<Perrsonne> participants;
	@Column(nullable = false)
	private String nom;
	private Localisation lieu;
	public Timestamp getDebut() {
		return debut;
	}
	public void setDebut(Timestamp debut) {
		this.debut = debut;
	}
	public Timestamp getFin() {
		return fin;
	}
	public void setFin(Timestamp fin) {
		this.fin = fin;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public ListePersonnes<?> getParticipants() {
		return participants;
	}
	public void setParticipants(ListePersonnes<Perrsonne> participants) {
		this.participants = participants;
	}
	public Localisation getLieu() {
		return lieu;
	}
	public void setLieu(Localisation lieu) {
		this.lieu = lieu;
	}
}
