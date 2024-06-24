package com.xantrix.webapp.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xantrix.webapp.entity.DettListini;

public interface PrezziRepository extends JpaRepository<DettListini, Long>
{
	
	//Query JPQL
	@Query(value = "SELECT d FROM Listini l JOIN DettListini d ON l.id = d.listino.id WHERE l.id = :idlist AND d.codArt = :codart")
	DettListini SelByCodArtAndList(@Param("codart") String CodArt, @Param("idlist") String Listino);

@Modifying
@Query(value = "DELETE FROM dettlistini WHERE CodArt = :codart AND IdList = :idlist", nativeQuery = true)
void DelRowDettList(@Param("codart") String CodArt, @Param("idlist") String IdList);

}