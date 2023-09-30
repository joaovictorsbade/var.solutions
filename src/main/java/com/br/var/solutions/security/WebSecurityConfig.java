package com.br.var.solutions.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/pessoa/authorization","/pessoa/authenticate", "/configuration/**","/webjars/**").permitAll()
                .anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS):
        httpSecurity.addFilterBefore();

    }

}
