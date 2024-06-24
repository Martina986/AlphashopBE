package com.xantrix.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.entity.Barcode;
import com.xantrix.webapp.exception.BindingException;
import com.xantrix.webapp.exception.DuplicateException;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.ArticoliService;
import com.xantrix.webapp.service.BarcodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("api/articoli")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticoliController {

    private static final Logger logger = LoggerFactory.getLogger(ArticoliController.class);

    @Autowired
    private ArticoliService articoliService;

    @Autowired
    private BarcodeService barcodeService;

    @Autowired
    private ResourceBundleMessageSource errMsg;
    @Autowired
    private PriceClient priceClient;
    @Autowired
    private PromoClient promoClient;

    private BigDecimal getPriceArt(String CodArt, String IdList, String Header) {

        BigDecimal prezzo = (!IdList.isEmpty()) ?
                priceClient.getPriceArt(Header, CodArt, IdList) :
                priceClient.getDefPriceArt(Header, CodArt);
        logger.info("Prezzo articolo " + CodArt + ": ", prezzo);

        return prezzo;
    }

    private BigDecimal getPriceArt(String CodArt, String IdList) {

        BigDecimal prezzo = (!IdList.isEmpty()) ?
                priceClient.getPriceArt( CodArt, IdList) :
                priceClient.getPriceArtNoAuth("");
        logger.info("Prezzo articolo " + CodArt + ": ", prezzo);

        return prezzo;
    }

    private BigDecimal getPromoArt(String CodArt, String Header)
    {

        BigDecimal prezzo = promoClient.getPromoArt(Header, CodArt);

        logger.info("Prezzo Promo Articolo " + CodArt + ": " + prezzo);

        return prezzo;
    }

    @GetMapping(value = "/test")
    public ResponseEntity<?> testConnex() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Connessione al WebService Articoli OK");

        return new ResponseEntity<>(responseNode.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/cerca/ean/{barcode}", produces = "application/json")
    public ResponseEntity<Articoli> listArtByEan(@PathVariable("barcode") String Barcode, HttpServletRequest httpRequest) throws NotFoundException {
        logger.info("****** Otteniamo l'articolo con barcode{}*******", Barcode);

        String authHeader = httpRequest.getHeader("Authorization");

        Articoli articoli;
        Barcode Ean = barcodeService.SelByBarcode(Barcode);

        if (Ean == null) {
            String ErrMsg = String.format("Il barcode %s non è stato trovato!", Barcode);
            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);
            //return new ResponseEntity<Articoli>(HttpStatus.NOT_FOUND);
        }
        else
        {
            articoli = Ean.getArticoli();
            articoli.setPrezzo(this.getPriceArt(articoli.getCodArt(), "", authHeader));
            articoli.setPromo(this.getPromoArt(articoli.getCodArt(), authHeader));
        }

        return new ResponseEntity<>(articoli, HttpStatus.OK);

    }

    @GetMapping(value = "/cerca/codice/{codart}", produces = "application/json")
    public ResponseEntity<Articoli> listArtByCodArt(@PathVariable("codart") String CodArt, HttpServletRequest httpRequest) throws NotFoundException{

        logger.info("****** Otteniamo l'articolo con codice {}*******", CodArt );

        String authHeader = httpRequest.getHeader("Authorization");

        Articoli articoli = articoliService.SelByCodArt(CodArt);

        if (articoli == null) {
            String ErrMsg = String.format("L'articolo con codice %s non è stato trovato!", CodArt);
            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        else {
            articoli.setPrezzo(this.getPriceArt(articoli.getCodArt(), "", authHeader));
            articoli.setPromo(this.getPromoArt(articoli.getCodArt(), authHeader));
        }

        return new ResponseEntity<>(articoli, HttpStatus.OK);

    }

    @GetMapping(value = "/noauth/cerca/codice/{codart}", produces = "application/json")
    public ResponseEntity<Articoli> GetArtNoAuth(@PathVariable("codart") String CodArt)
            throws NotFoundException
    {
        logger.info("****** Otteniamo l'articolo con codice " + CodArt + " *******");

        Articoli articolo = articoliService.SelByCodArt(CodArt);

        if (articolo == null)
        {
            String ErrMsg = String.format("L'articolo con codice %s non è stato trovato!", CodArt);

            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        else
        {
            articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(), ""));
        }

        return new ResponseEntity<>(articolo, HttpStatus.OK);
    }

    @GetMapping(value = "/cerca/descrizione/{filter}", produces = "application/json")
    public ResponseEntity<List<Articoli>> listArtByDesc(@PathVariable("filter") String Filter, HttpServletRequest httpRequest) throws NotFoundException {

        String AuthHeader = httpRequest.getHeader("Authorization");

        List<Articoli> articoli = articoliService.SelByDescrizione(Filter.toUpperCase() + "%");

        if (articoli == null)
        {
            String ErrMsg = String.format("Non è stato trovato alcun articolo avente descrizione %s", Filter);

            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);

        }
        else
        {
            articoli.forEach(f -> f.setPrezzo(this.getPriceArt(f.getCodArt(), "", AuthHeader)));
            articoli.forEach(f -> f.setPromo(this.getPromoArt(f.getCodArt(), AuthHeader)));
        }

        return new ResponseEntity<>(articoli, HttpStatus.OK);
    }

    @PostMapping(value ="/inserisci", produces = "application/json")
    public ResponseEntity<?> createArt(@Validated @RequestBody Articoli articoli, BindingResult bindingResult) throws BindingException, DuplicateException {

        logger.info("****** Salviamo l'articolo con codice {} *******", articoli.getCodArt());

        if (bindingResult.hasErrors())
        {
            String msgErr = errMsg.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());

            logger.warn(msgErr);
            throw new BindingException(msgErr);
        }

        //Disabilitare se si vuole gestire anche la modifica
        Articoli checkArt = articoliService.SelByCodArt(articoli.getCodArt());

        if (checkArt != null) {

            String msgErr = String.format("Articolo %s presente in anagrafica! " + "Impossibile utilizzare il metodo POST", articoli.getCodArt());

            logger.warn(msgErr);

            throw new DuplicateException(msgErr);
        }

        articoliService.InsArticolo(articoli);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.CREATED.toString());
        responseNode.put("message", "Inserimento Articolo " + articoli.getCodArt() + " Eseguita Con Successo");


        return new ResponseEntity<>(responseNode, HttpStatus.CREATED);
    }

    @PutMapping("/modifica")
    public ResponseEntity<?> updateArt(@Validated @RequestBody Articoli articoli, BindingResult bindingResult)
            throws BindingException, NotFoundException {

        logger.info("Modifichiamo l'articolo con codice {} *******", articoli.getCodArt());

        if(bindingResult.hasErrors())
        {
            String msgErr = errMsg.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());

            logger.warn(msgErr);
            throw new BindingException(msgErr);
        }
        Articoli checkArt = articoliService.SelByCodArt(articoli.getCodArt());

        if (checkArt == null) {

            String msgErr = String.format("Articolo %s non presente in anagrafica! " + "Impossibile utilizzare il metodo PUT", articoli.getCodArt());

            logger.warn(msgErr);

            throw new NotFoundException(msgErr);
        }

        articoliService.InsArticolo(articoli);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Modifica Articolo " + articoli.getCodArt() + " Eseguita Con Successo");


        return new ResponseEntity<>(responseNode, HttpStatus.OK);
    }

    @DeleteMapping("/elimina/{codArt}")
    public ResponseEntity<?> deleteArt(@PathVariable("codArt") String codArt) throws NotFoundException {

        logger.info("Eliminamo l'articolo con codice {} *******", codArt);

        Articoli articolo = articoliService.SelByCodArt(codArt);

        if (articolo == null) {

            String msgErr = String.format("Articolo %s non presente in anagrafica! ", codArt);

            logger.warn(msgErr);
            throw new NotFoundException(msgErr);
        }

        articoliService.DelArticolo(articolo);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione Articolo " + codArt + " Eseguita Con Successo");

        return new ResponseEntity<>(responseNode, HttpStatus.OK);
    }
}
