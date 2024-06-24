package com.xantrix.webapp.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;

@FeignClient(name ="PromoWebService", url="localhost:8091") 
public interface PromoClient 
{
	@GetMapping(value = "api/promo/prezzo/{codart}")
    BigDecimal getPromoArt(@RequestHeader("Authorization") String AuthHeader, @PathVariable("codart") String CodArt);
	
	@GetMapping(value = "api/promo/prezzo/fidelity/{codart}")
     BigDecimal getPromoArtFid(@RequestHeader("Authorization") String AuthHeader, @PathVariable("codart") String CodArt);
	
	@GetMapping(value = "api/promo/prezzo/{codart}/{codfid}")
     BigDecimal getPromoOnlyFid(@RequestHeader("Authorization") String AuthHeader, @PathVariable("codart") String CodArt, @PathVariable("codfid") String CodFid);
	
}
