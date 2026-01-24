package com.br.financialmanager.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.financialmanager.domain.entities.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
  private String secret = "minha-chave-secreta-super-dificil"; // Em prod, isso vem do application.properties

  public String gerarToken(Usuario usuario) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
        .withIssuer("financial-manager")
        .withSubject(usuario.getCpf()) // O CPF será o ID no token
        .withExpiresAt(dataExpiracao())
        .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Erro ao gerar token", exception);
    }
  }

  public String validarToken(String tokenJWT) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
        .withIssuer("financial-manager")
        .build()
        .verify(tokenJWT)
        .getSubject();
    } catch (JWTVerificationException exception) {
      return "";
    }
  }

  private Instant dataExpiracao() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
