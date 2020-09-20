package fr.iconvoit.entity;

import java.security.Timestamp;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;

import org.springframework.data.annotation.Id;

//@EntityScan
public class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private Timestamp start;
	@Column(nullable = false)
	private Timestamp end;
	@ManyToMany()
	private ArrayList<People> participants;
	@Column(nullable = false)
	private String name;
	private Localisation position;
	public Timestamp getDebut() {
		return start;
	}
	public void setDebut(Timestamp debut) {
		this.start = debut;
	}
	public Timestamp getFin() {
		return end;
	}
	public void setFin(Timestamp fin) {
		this.end = fin;
	}
	public String getNom() {
		return name;
	}
	public void setNom(String nom) {
		this.name = nom;
	}
	public ArrayList<?> getParticipants() {
		return participants;
	}
	public void setParticipants(ArrayList<People> participants) {
		this.participants = participants;
	}
	public Localisation getLieu() {
		return position;
	}
	public void setLieu(Localisation lieu) {
		this.position = lieu;
	}
}
