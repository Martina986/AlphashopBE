package com.xantrix.webapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.entities.Promo;
import com.xantrix.webapp.repository.PromoRepository;

@Service
@Transactional
public class PromoServiceImpl implements PromoService
{
	@Autowired
	PromoRepository promoRepository;

	@Override
	public List<Promo> SelTutti() 
	{
		return promoRepository.findAll();
	}

	@Override
	public Promo SelByIdPromo(String IdPromo) 
	{
		return promoRepository.findByIdPromo(IdPromo);
	}

	public Promo SelByAnnoCodice(int anno, String codice) {
		return promoRepository.findByAnnoAndCodice(anno, codice);
	}

	@Override
	public List<Promo> SelPromoActive() 
	{
		return promoRepository.SelPromoActive();
	}

	@Override
	public void InsPromo(Promo promo) 
	{
		promoRepository.saveAndFlush(promo);
	}

	@Override
	public void DelPromo(Promo promo) 
	{
		promoRepository.delete(promo);
	}

}
