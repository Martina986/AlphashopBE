package com.xantrix.webapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> exceptionNotFoundHandler(Exception ex){

        ErrorResponse errore = new ErrorResponse();

        errore.setCodice(HttpStatus.NOT_FOUND.value());
        errore.setMessaggio(ex.getMessage());

        return new ResponseEntity<>(errore, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindingException.class)
    public ResponseEntity<ErrorResponse> exceptionBindingHandler(Exception ex){
        ErrorResponse errore = new ErrorResponse();

        errore.setCodice(HttpStatus.BAD_REQUEST.value());
        errore.setMessaggio(ex.getMessage());

        return new ResponseEntity<>(errore, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> exceptionDuplicateHandler(Exception ex){

        ErrorResponse errore = new ErrorResponse();

        errore.setCodice(HttpStatus.NOT_ACCEPTABLE.value());
        errore.setMessaggio(ex.getMessage());

        return new ResponseEntity<>(errore, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }


}
