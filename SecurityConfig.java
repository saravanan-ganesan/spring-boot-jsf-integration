package com.jsf.samples.bootfaces.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 
 * This configuration class is used Configures our application with Spring Security 
 * to restrict access to our API end points.
 *
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    ClientRegistrationRepository clientRegistrationRepository;
	
	
    public void configure(HttpSecurity http) throws Exception {
    	
    	http.csrf().disable();
    	
    	http.headers()
        	.addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(
                new AntPathRequestMatcher("/javax.faces.resource/**"), new HeaderWriter() {

                    @Override
                    public void writeHeaders(HttpServletRequest request,
                            HttpServletResponse response) {
                        response.addHeader("Cache-Control", "private, max-age=86400");
                    }
                }))
        .defaultsDisabled();
    	
    	http.authorizeRequests(authorizeRequests -> authorizeRequests
    			.antMatchers("/favicon.ico", "/javax.faces.resource/**").permitAll()
    			.antMatchers("/favicon.ico", "/actuator/**", "/login", "/logout",  "/auth/login", 
    					"/auth/logout", "/auth/logged", "/*.html", "/images/**", "/js/**", "/css/**", "/fcis_logo.jpg").permitAll()
    			.antMatchers("/**").hasAuthority("SCOPE_rise-app")
    			.anyRequest().authenticated())
    			.exceptionHandling().accessDeniedPage("/accessDenied")
//    			.authenticationEntryPoint(new CustomEntryPoint())
   			.and()
    			.oauth2Login(Customizer.withDefaults())
    			
    	.logout(logout -> logout                                                
                .logoutUrl("/logout")     
                .invalidateHttpSession(true)          
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")    
                .logoutSuccessUrl("/login?logout=true")     
                .logoutSuccessHandler(new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository))
         )
    	.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    

}