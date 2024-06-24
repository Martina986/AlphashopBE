package com.xantrix.webapp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.validation.constraints.Min;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dettlistini")
@Getter
@Setter
public class DettListini implements Serializable
{
	private static final long serialVersionUID = 8777751177774522519L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)// postgres non gestisce questa colonna.
	@Column(name = "ID")
	private Integer id;
	
	@Size(min = 5, max = 20, message = "{Size.DettListini.codArt.Validation}")
	@NotNull(message = "{NotNull.DettListini.codArt.Validation}")
	@Column(name = "CODART",  length = 20)
	private String codArt;

	@DecimalMin(value = "0.01", message = "{Min.DettListini.prezzo.Validation}")
	@Column(name = "PREZZO")
	private BigDecimal prezzo;
	
	@ManyToOne
	@EqualsAndHashCode.Exclude
	@JoinColumn(name = "IDLIST", referencedColumnName = "id")
	@JsonBackReference
	private Listini listino;
	
	public DettListini() {}
	
	public DettListini(String CodArt, BigDecimal Prezzo, Listini Listino)
	{
		this.codArt = CodArt;
		this.prezzo = Prezzo;
		this.listino = Listino;
	}
}
