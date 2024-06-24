package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name ="BARCODE")
@Data
public class Barcode implements Serializable {

    private static final long serialVersionUID = 1853763261962860635L;

    @Id
    @Column(name = "BARCODE")
    @Size(min = 8, max = 15, message = "{Size.Barcode.barcode.Validation}")
    @NotNull(message = "{NotNull.Barcode.barlidation}")
    private String barcode;

    @Column(name ="IDTIPOART")
    private String idTipoArt;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name ="CODART", referencedColumnName = "codArt")
    @JsonBackReference//codificatore per json //punto di arrivo
    private Articoli articoli;
}
