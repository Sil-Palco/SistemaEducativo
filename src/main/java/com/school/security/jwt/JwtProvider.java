package com.school.security.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.school.security.dto.JwtDto;
import com.school.security.models.UsuarioPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

	private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class); 
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private int expiration;
	
	public String generatToken(Authentication auth) {
		
		UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) auth.getPrincipal();
		List<String> roles = usuarioPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()); 
		
		return Jwts.builder().setSubject(usuarioPrincipal.getUsername())
							 .claim("roles", roles)
							 .setIssuedAt(new Date())
							 .setExpiration(new Date(new Date().getTime() + expiration))
							 .signWith(SignatureAlgorithm.HS512, secret.getBytes())
							 .compact();
	}
	
	public String getUsernameFromToken(String token) {
		return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	}
	
	public Boolean validateToken(String token) {
		
		try {
			Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
			return true;
		}catch(MalformedJwtException e) {
			logger.error(" token mal formado");
		}catch(UnsupportedJwtException e) {
			logger.error(" token no soportado");
		}catch(ExpiredJwtException e) {
			logger.error(" token expirado");
		}catch(IllegalArgumentException e) {
			logger.error(" token vacio");
		}catch(SignatureException e) {
			logger.error(" fail en a firma");
		}
		
		return false;
	}
	
	public String refreshToken(JwtDto jwtDto) throws ParseException {

		try {
			Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(jwtDto.getToken());
		} catch (ExpiredJwtException e) {

			JWT jwt = JWTParser.parse(jwtDto.getToken());
			JWTClaimsSet claims = jwt.getJWTClaimsSet();
			String username = claims.getSubject();
			List<String> roles = (List<String>) claims.getClaim("roles");

			return Jwts.builder().setSubject(username)
					.claim("roles", roles)
					.setIssuedAt(new Date())
					.setExpiration(new Date(new Date().getTime() + expiration))
					.signWith(SignatureAlgorithm.HS512, secret.getBytes())
					.compact();
		}
		
		return null;
	}
	
	
}
