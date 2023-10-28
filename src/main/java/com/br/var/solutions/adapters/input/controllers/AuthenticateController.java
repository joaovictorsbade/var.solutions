package com.br.var.solutions.adapters.input.controllers;

import com.br.var.solutions.GenerateToken;
import com.br.var.solutions.ValidaUsuario;
import com.br.var.solutions.infraestructure.config.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Slf4j

public class AuthenticateController {
    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<GenerateToken> generateToken (@RequestParam("client_id") String client_id,
                                                        @RequestParam("password") String password)
    {
        log.info("Bora tentar ne gerar o token se der certo blz se não der paciência");
        Boolean validaUsuario = ValidaUsuario.validaUsuario(client_id,password);

        if(Boolean.FALSE.equals(validaUsuario))
        {
            log.info("Não foi possível gerar o token, pois o usario ou a senha são incorretos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenerateToken());
        }
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        String token = jwtTokenUtil.generateToken(client_id);


        GenerateToken tokenResponse = new GenerateToken();
        tokenResponse.setToken(token);
        tokenResponse.setExpiraEm(tokenResponse.getExpiraEm());

        log.info("Seu token ta gerado pelo user :" + client_id + " em" + System.currentTimeMillis());
        return ResponseEntity.ok(tokenResponse);
    }
}
