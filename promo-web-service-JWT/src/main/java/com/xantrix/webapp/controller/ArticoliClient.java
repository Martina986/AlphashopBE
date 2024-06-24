package com.xantrix.webapp.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xantrix.webapp.entities.Articoli;

@FeignClient(name ="ArticoliWebService", url="http://localhost:5051") //, configuration = FeignClientConfiguration.class)
public interface ArticoliClient 
{
	@GetMapping(value = "api/articoli/noauth/cerca/codice/{codart}")
    Articoli getArticolo(@PathVariable("codart") String CodArt);
	
}
