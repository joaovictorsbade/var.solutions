package com.br.var.solutions.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -1123;

    public static final long JWT_TOKEN_EXPIRES = 1800000;

    private String secret = " bc2784cebe53d34a2219b42aeee52575f7573fd2b1bbdd17d081b75eec0be45b4379818cd4bd41cd343f82e44037f0ec471bf904927cd4ba8d702e0b6e0934b9";

    //retorna o username do token do jwt
    public String getUsernameFronToken(String token){
        return getClainFromToken(token, Claims::getSubject);
    }

    //retorna varios objetos possiveis de varios tipos possiveis. - captura informações de dentro do token.
    public <T> T getClainFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //retorna expiration date do token jwt
    public Date getExpirationDateFromToken(String token){
        return getClainFromToken(token, Claims::getExpiration);
    }

    //para retorna qualquer informação do token, e para isso nós vamos prescisar da secret
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
    }

    //validar se o token é expirado.
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //Gerar Token
    public String generateToken(String clientId){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, clientId);
    }

    private String doGenerateToken(Map<String, Object> claims, String clientId) {
        return Jwts.builder().setClaims(claims).setSubject(clientId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_EXPIRES))
                .signWith(SignatureAlgorithm.ES512, secret).compact();
    }

    //valida p token
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFronToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
