package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.DettListini;
 
public interface PrezziService
{
	DettListini SelPrezzo(String CodArt, String Listino);
	
	void DelPrezzo(String CodArt, String IdList);

}
