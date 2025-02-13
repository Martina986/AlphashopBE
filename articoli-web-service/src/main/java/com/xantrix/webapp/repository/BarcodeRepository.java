package com.xantrix.webapp.repository;

import com.xantrix.webapp.entity.Barcode;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BarcodeRepository extends JpaRepository<Barcode, String> {

    Barcode findByBarcode(String Barcode);

}
