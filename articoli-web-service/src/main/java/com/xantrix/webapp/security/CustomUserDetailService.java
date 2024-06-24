package com.xantrix.webapp.security;

import com.xantrix.webapp.model.Utenti;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service("customUserDetailService")
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    @Autowired
    private UserConfig userConfig;

    @Override
    public UserDetails loadUserByUsername(String UserId) throws UsernameNotFoundException {

        String ErrMsg = "";

        if (UserId == null || UserId.length() < 2) {
            ErrMsg = "Username non valido";

            logger.warn(ErrMsg);

            throw new UsernameNotFoundException(ErrMsg);
        }

        Utenti utente = this.getHttpValue(UserId);

        if (utente == null) {
            ErrMsg = String.format("Utente %s non trovato! ", UserId);

            logger.warn(ErrMsg);

            throw new UsernameNotFoundException(ErrMsg);
        }

        User.UserBuilder builder = null;
        builder = User.withUsername(UserId);
        builder.disabled(utente.getAttivo().equals("Si") ? false : true);
        builder.password(utente.getPassword());

        String[] profili = utente.getRuoli()
                .stream().map(a -> "ROLE_" + a).toArray(String[]::new);

        builder.authorities(profili);

        return builder.build();
    }

    private Utenti getHttpValue(String UserId) {

        URI url = null;

        try {
            String SvrUrl = userConfig.getSrvUrl();
            url = new URI(SvrUrl + UserId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userConfig.getUserId(), userConfig.getPassword()));

        Utenti utente = null;

        try {

            utente = restTemplate.getForObject(url, Utenti.class);
        } catch (Exception e) {
            String ErrMsg = String.format("Connessione al servizio di autenticazione non riuscita!!");
            logger.warn(ErrMsg);
        }

        return utente;
    }
}