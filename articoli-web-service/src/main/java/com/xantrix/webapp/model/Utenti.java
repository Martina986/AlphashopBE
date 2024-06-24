package com.xantrix.webapp.model;

import lombok.Data;

import java.util.List;

@Data
public class Utenti {

    private String id;
    private String userId;
    private String password;
    private String attivo;
    private List<String> ruoli;
}
