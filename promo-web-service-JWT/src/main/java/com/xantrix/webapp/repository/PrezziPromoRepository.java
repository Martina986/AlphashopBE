package com.xantrix.webapp.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xantrix.webapp.entities.DettPromo;

public interface PrezziPromoRepository extends JpaRepository<DettPromo, Long>
{
	
	//Query JPQL - Selezione promo per codice articolo
	@Query(value = "SELECT d FROM Promo p JOIN DettPromo d ON d.promo.idPromo = p.idPromo WHERE d.codart = :codart AND d.isfid = 'No' AND CURRENT_DATE BETWEEN d.inizio AND d.fine")
	DettPromo SelByCodArt(@Param("codart") String CodArt);
	
	
	//Query JPQL - Selezione promo fidelity per codice articolo
	@Query(value = "SELECT d FROM Promo p JOIN DettPromo d ON d.promo.idPromo = p.idPromo WHERE d.codart = :codart AND d.isfid = 'Si' AND CURRENT_DATE BETWEEN d.inizio AND d.fine")
	DettPromo SelByCodArtAndFid(@Param("codart") String CodArt);
	
		
	//Query JPQL - Selezione promo per codice fidelity e codice
	@Query(value = "SELECT d FROM Promo p JOIN DettPromo d ON d.promo.idPromo = p.idPromo WHERE d.codart = :codart AND d.codfid = :codfid AND " +
				   "CURRENT_DATE BETWEEN d.inizio AND d.fine")
	DettPromo SelByCodArtAndCodFid(@Param("codart") String CodArt, @Param("codfid") String CodFid);
	
	/*
	@Query("SELECT DISTINCT b FROM Promo a JOIN a.dettPromo b WHERE CURRENT_DATE BETWEEN b.inizio AND b.fine")
	List<DettPromo> SelPromoActive();
	
	//Query SQL - Modifica oggetto promozione (prezzo)
	@Modifying
	@Query(value = "UPDATE dettpromo SET OGGETTO = :oggetto WHERE ID = :id", nativeQuery = true)
	void UpdOggettoPromo(@Param("oggetto") String Oggetto, @Param("id") Long Id);
	*/

		
}
