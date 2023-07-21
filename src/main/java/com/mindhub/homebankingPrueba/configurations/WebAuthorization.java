package com.mindhub.homebankingPrueba.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

@EnableWebSecurity
@Configuration
public class WebAuthorization {


    @Bean
    public DefaultSecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/pages/index.html","/web/js/**","/web/assets/images/**","/web/assets/styles/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/login", "/api/payments/**", "/api/payments").permitAll()
                .antMatchers(HttpMethod.POST,"/api/logout").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients", "/api/transactions", "/api/loans").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts", "/api/clients/current/cards", "/api/clients/cards/renew/**","/api/client/loan/payments", "/api/client/loan/payments/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards/**", "/api/accounts/delete/**").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current","/api/accounts/**","/api/loans" ).permitAll()
                .antMatchers("/web/pages/account.html","/api/clients/current/**","/api/clients/accounts", "/api/client/loans/**" ,"/web/pages/accounts.html","/web/pages/cards.html","/web/pages/editProfile.html","/web/pages/wallet.html").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/loans/**").hasAuthority("ADMIN")
                .antMatchers("/rest/**", "/api/**", "/web/pages/manager.html").hasAuthority("ADMIN");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();
        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();
        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.cors();

        return http.build();
    }
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }

}

