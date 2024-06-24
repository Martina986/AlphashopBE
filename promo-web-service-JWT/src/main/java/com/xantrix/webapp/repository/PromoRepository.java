package com.xantrix.webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xantrix.webapp.entities.Promo;

@Repository
public interface PromoRepository extends JpaRepository<Promo, String>
{
	Promo findByIdPromo(String IdPromo);
	
	Promo findByAnnoAndCodice(int Anno, String Codice);
	
	
	@Query("SELECT DISTINCT p FROM Promo p JOIN DettPromo d ON d.promo.idPromo = p.idPromo WHERE CURRENT_DATE BETWEEN d.inizio AND d.fine")
	List<Promo> SelPromoActive();

}
