package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.Articoli;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticoliService {

   Iterable<Articoli> SelTutti();

    List<Articoli> SelByDescrizione(String descrizione);

    List<Articoli> SelByDescrizione(String descrizione, Pageable pageable);

    Articoli SelByCodArt(String codArt);

    void DelArticolo(Articoli articolo);

    void InsArticolo(Articoli articolo);
}
