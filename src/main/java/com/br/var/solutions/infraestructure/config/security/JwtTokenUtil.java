package com.br.var.solutions.infraestructure.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_EXPIRES = 1800000;

    private String secret = "a826f4d413a38a825d9dc3d0d274c2da031e8c3bcf3673cfefcd1a6105ea86068171bc72222c3b5017672e3fedd717fe1811dbe43b918df4ee025fa4a83dbb9d";

    public String getUserNameFronToken(String token)
    {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retorna varios objetos possiveis de varios tipos possiveis - captura as informaçõe de dentro do token
    public <T> T  getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFronToken(token);
        return claimsResolver.apply(claims);
    }

    // retorna expiration date do token jwt
    public Date getExpirationDateFronToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //para retornar qualquer informacao do token para isso nos vamos precisar da secret
    private Claims getAllClaimsFronToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //validar se o token é xepirado.
    private Boolean isTokenExpirado(String token){
        final Date expiration = getExpirationDateFronToken(token);
        return expiration.before(new Date());
    }

    // Gerar o token
    public String generateToken(String client_id){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, client_id);
    }
    //Cria o token e define o tempo de expiração
    private String doGenerateToken(Map<String, Object> claims, String clientId) {
        return Jwts.builder().setClaims(claims).setSubject(clientId).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRES))
                .signWith(SignatureAlgorithm.HS512,secret).compact();
    }

    //valida o token
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUserNameFronToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpirado(token));
    }






}