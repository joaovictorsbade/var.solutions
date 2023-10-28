package com.br.var.solutions.infraestructure.config.security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable{
    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authExeption) throws IOException{
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");

    }
}