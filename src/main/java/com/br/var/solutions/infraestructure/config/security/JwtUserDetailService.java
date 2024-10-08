package com.br.var.solutions.infraestructure.config.security;

import com.br.var.solutions.ValidaUsuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailService implements UserDetailsService
{
    @Override
    public UserDetails loadUserByUsername(String userName)
    {
        String userValido = ValidaUsuario.returnUsername(userName);
        if (userValido != null)
        {
            String password = ValidaUsuario.returnPassword(userName);
            return new User(userName, password, new ArrayList<>());
        }
        else
        {
            throw new UsernameNotFoundException("Nenhum usuario encontrado com esta propriedade de token, user: " + userName);
        }
    }
}


