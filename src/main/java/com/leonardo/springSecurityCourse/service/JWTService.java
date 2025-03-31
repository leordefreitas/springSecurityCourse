package com.leonardo.springSecurityCourse.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {


    private String secrectKey = "";


    // aqui o professor ensinou como criar uma key automacitcamente
    public JWTService() {
        try {
            // o tipo dela e secretkey nao string
            KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keygen.generateKey();
            // aqui ele passa de secretkey para string por isso este codigo
            secrectKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30)) // 30 minutos
                .and()
                .signWith(getkey())
                .compact();
    }


    private SecretKey getkey() {
        // transformando a chave em bytes pois o hmac precisa ser passado
        // em bytes
        byte[] keyBytes = Decoders.BASE64.decode(secrectKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // o codigo que esta abaixo foi feito pelo professor no qual ele conseguiu retirar
    // os dados do token, lembrando que nao e uma tarefa facil tem que ser realizado ainda
    // no dedo, passo a passo para que funcione
    // para retirar as coisas do toke e necessario fazer este passo a passo
    // codigo copiado do profesor, ele mesmo depois explicou estes codigos
    public String extractUserName(String token) {
        return extactClaim(token, Claims::getSubject);
    }

    private <T> T extactClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getkey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extactClaim(token, Claims::getExpiration);
    }
}
