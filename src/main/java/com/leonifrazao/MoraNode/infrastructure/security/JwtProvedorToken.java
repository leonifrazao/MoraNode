package com.leonifrazao.MoraNode.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtProvedorToken {

    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.access-token-expiration}")
    private long expiracaoAccessToken;

    @Value("${jwt.refresh-token-expiration}")
    private long expiracaoRefreshToken;

    public String gerarAccessToken(String email, String papel) {
        return gerarToken(email, papel, expiracaoAccessToken);
    }

    public String gerarRefreshToken(String email) {
        return gerarToken(email, null, expiracaoRefreshToken);
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public boolean tokenValido(String token) {
        try {
            Jwts.parser()
                    .verifyWith(obterChave())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String gerarToken(String email, String papel, long expiracao) {
        var agora = new Date();
        var validade = new Date(agora.getTime() + expiracao);

        var builder = Jwts.builder()
                .subject(email)
                .issuedAt(agora)
                .expiration(validade);

        if (papel != null) {
            builder.claim("papel", papel);
        }

        return builder.signWith(obterChave()).compact();
    }

    private <T> T extrairClaim(String token, Function<Claims, T> extrator) {
        Claims claims = Jwts.parser()
                .verifyWith(obterChave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return extrator.apply(claims);
    }

    private SecretKey obterChave() {
        byte[] chaveBytes = Decoders.BASE64.decode(segredo);
        return Keys.hmacShaKeyFor(chaveBytes);
    }
}
