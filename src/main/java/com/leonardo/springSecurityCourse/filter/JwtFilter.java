package com.leonardo.springSecurityCourse.filter;

import com.leonardo.springSecurityCourse.service.JWTService;
import com.leonardo.springSecurityCourse.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


// tem que ser declarado como component para funcionar o autowired
@Component
// para adicionar o filtro quando vem o request de seguranca cria-se a classe
// aqui passaremos as orientacos para aplicacao cumprir
// OncePerRequestFilter quer dizer que passara no filtro somente uma vez
// tanto para entrada e saida de informacoes do banco de dados
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    ApplicationContext context;



    // este e o lugar onde configuramos como devera ser o comportamento do filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // o que recebemos do header
        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsdSIsImlhdCI6MTc0MzM0ODg2MiwiZXhwIjoxNzQzMzQ4OTcwfQ.3JvnQpnQgAo5d8rcQKhnl3ktVwe_tyDyLdphn5hsIWw

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            // aqui ele esta cortando a infomacao que ele pegou no header, para ficar
            // somente o codigo do token
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }


        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = context.getBean(MyUserDetailsService.class)
                    .loadUserByUsername(username);

            if(jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
