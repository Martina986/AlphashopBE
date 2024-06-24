package com.xantrix.webapp.exception;


import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class DuplicateException extends Exception{


    private static final long serialVersionUID = 1L;

    private String messaggio;

    public DuplicateException(String messaggio) {
        super(messaggio);
    }


}
