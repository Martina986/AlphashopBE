package com.xantrix.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.constraints.DecimalMin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.appconf.AppConfig;
import com.xantrix.webapp.entity.DettListini;
import com.xantrix.webapp.service.PrezziService;

@RestController
@RequestMapping("/api/prezzi")
public class PrezziController
{
	private static final Logger logger = LoggerFactory.getLogger(PrezziController.class);

	@Autowired
	private PrezziService prezziService;
	
	@Autowired
	private AppConfig Config;

	@GetMapping(value = {"/noauth/{codart}/{idlist}", "/noauth/{codart}"})
	public BigDecimal getPriceCodArtNoAuth(@PathVariable("codart") String CodArt, @PathVariable("idlist") Optional<String> optIdList) {

		BigDecimal retVal = BigDecimal.valueOf(0);

		String IdList = optIdList.orElseGet(() -> Config.getListino());

		logger.info("Listino di Riferimento: " + IdList);

		DettListini prezzo =  prezziService.SelPrezzo(CodArt, IdList);

		if (prezzo != null)
		{
			logger.info("Prezzo Articolo: " + prezzo.getPrezzo());
			retVal = prezzo.getPrezzo();
		}
		else
		{
			logger.warn("Prezzo Articolo Assente!!");
		}

		return retVal;
	}

	@GetMapping(value = {"/{codart}/{idlist}", "/{codart}"})
	public BigDecimal getPriceCodArt(@PathVariable("codart") String CodArt, @PathVariable("idlist") Optional<String> optIdList)
	{
		BigDecimal retVal = BigDecimal.valueOf(0);
												//se				//altrimenti
		String IdList = (optIdList.isPresent()) ? optIdList.get() : Config.getListino();

		logger.info("Listino di Riferimento: " + IdList);

		DettListini prezzo =  prezziService.SelPrezzo(CodArt, IdList);

		if (prezzo != null)
		{
			logger.info("Prezzo Articolo: " + prezzo.getPrezzo());
			retVal = prezzo.getPrezzo();
		}
		else
		{
			logger.warn("Prezzo Articolo Assente!!");
		}

		return retVal;
	}

	@DeleteMapping(value = "/elimina/{codart}/{idlist}")
	public ResponseEntity<?> deletePrice(@PathVariable("codart") String CodArt, @PathVariable("idlist") String IdList)
	{
		logger.info(String.format("Eliminazione prezzo listino %s dell'articolo %s",IdList,CodArt));

		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();

		prezziService.DelPrezzo(CodArt, IdList);

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Eliminazione Prezzo Eseguita Con Successo");
		
		logger.info("Eliminazione Prezzo Eseguita Con Successo");

		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
	

}
