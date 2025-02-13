package com.xantrix.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

	@Autowired
	@Qualifier("customUserDetailService")
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${sicurezza.uri}")
	private String authenticationPath;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}


	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception 
	{
		return super.authenticationManagerBean();
	}
	
	private static final String[] NOAUTH_MATCHER = {"/api/articoli/noauth/cerca/codice/**"};
	private static final String[] USER_MATCHER = { "/api/articoli/cerca/**"};
	private static final String[] ADMIN_MATCHER = { "/api/articoli/inserisci/**", 
			"/api/articoli/modifica/**", "/api/articoli/elimina/**" };

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
		.exceptionHandling().authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint)
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers(NOAUTH_MATCHER).permitAll()
		.antMatchers(USER_MATCHER).hasAnyRole("USER")
		.antMatchers(ADMIN_MATCHER).hasAnyRole("ADMIN")
		.anyRequest().authenticated();

		httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		httpSecurity.headers().frameOptions()
		.sameOrigin().cacheControl();  
	}
	
	@Override
	public void configure(WebSecurity webSecurity) throws Exception 
	{
		webSecurity.ignoring().antMatchers(HttpMethod.POST, authenticationPath)
				.antMatchers(HttpMethod.OPTIONS, "/**")
				.and().ignoring()
				.antMatchers(HttpMethod.GET, "/");	
	}
}
