package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "INGREDIENTI")
@Data
public class Ingredienti implements Serializable {

    private static final long serialVersionUID = -6239322605771693808L;

    @Id
    @Column(name = "CODART")
    private String codArt;

    @Column(name = "INFO")
    private String info;

    @OneToOne
    @PrimaryKeyJoinColumn
    @JsonIgnore // è l'equivalente alla jsn reference //punto di arrivo
    private Articoli articoli;


}
