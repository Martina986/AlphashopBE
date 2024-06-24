package com.xantrix.webapp.service;

import java.util.List;

import com.xantrix.webapp.model.Utenti;
 
public interface UtentiService
{
	 List<Utenti> SelTutti();
	
	 Utenti SelUser(String UserId);
	
	 void Save(Utenti utente);
	
	 void Delete(Utenti utente);
	
}
