package com.xantrix.webapp.service;

import java.util.Optional;

import com.xantrix.webapp.entity.Listini;

public interface ListinoService 
{
	Optional<Listini> SelById(String Id);
	
	void InsListino(Listini listino);

	void DelListino(Listini listino);
}
