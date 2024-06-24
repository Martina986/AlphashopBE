package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ARTICOLI")
@Data
public class Articoli implements Serializable {

    private static final long serialVersionUID = 291353626011036772L;

    @Id
    @Column(name = "CODART")
    @Size(min = 5, max = 20, message = "{Size.Articoli.codArt.Validation}")
    @NotNull(message = "{NotNull.Articoli.codArt.Validation}")
    private String codArt;

    @Column(name = "DESCRIZIONE")
    @NotNull(message = "{NotNull.Articoli.descrizione.Validation}")
    @Size(min = 6, max = 80, message = "{Size.Articoli.descrizione.Validation}")
    private String descrizione;

    @Column(name = "UM")
    @NotNull(message = "{NotNull.Articoli.um.Validation}")
    private String um;

    @Column(name = "CODSTAT")
    private String codStat;

    @Column(name = "PZCART")
    @Max(value = 99, message = "{Max.Articoli.pzCart.Validation}")
    private Integer pzCart;

    @Column(name = "PESONETTO")
    @DecimalMin(value = "0.01", message = "{Min.Articoli.pesoNetto.Validation}")
    @DecimalMax(value = "100", message = "{Max.Articoli.pesoNetto.Validation}")
    private Double pesoNetto;

    @Column(name = "IDSTATOART")
    @NotNull(message = "{NotNull.Articoli.idStatoArt.Validation}")
    private String idStatoArt;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATACREAZIONE")
    private Date dataCreaz;

    @Transient
    private BigDecimal prezzo;

    @Transient
    private BigDecimal promo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "articoli", orphanRemoval = true)
    @JsonManagedReference//codificatore per json //punto di partenza
    private Set<Barcode> barcode = new HashSet<>();

    @OneToOne(mappedBy = "articoli", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ingredienti ingredienti;

    @ManyToOne
    @JoinColumn(name = "IDIVA", referencedColumnName = "idIva")
    private Iva iva;

    @ManyToOne
    @NotNull(message = "{NotNull.Articoli.famAssort.Validation}")
    @JoinColumn(name = "IDFAMASS", referencedColumnName = "id")
    private FamAssort famAssort;
}
