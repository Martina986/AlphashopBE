package com.xantrix.webapp.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.Data;

@Entity
@Table(name = "listini")
@Data
public class Listini implements Serializable
{ 
	private static final long serialVersionUID = 1891268953233014092L;
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Size(min = 10, max = 30, message = "{Size.Listini.descrizione.Validation}")
	@Column(name = "descrizione", length = 30)
	private String descrizione;

	@Size(min = 1, max = 50 , message = "{Size.Listini.obsoleto.Validation}")
	@Column(name = "obsoleto", length = 50)
	private String obsoleto;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "listino")
	@JsonManagedReference
	private Set<DettListini> dettListini = new HashSet<>();
	
	public Listini() {}
	
	public Listini(String Id, String Descrizione, String Obsoleto)
	{
		this.id = Id;
		this.descrizione = Descrizione;
		this.obsoleto = Obsoleto;
	}

}
