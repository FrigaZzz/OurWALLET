package com.mfr.taass.spring.group.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component // TODO: ci sono ancora dei metodi da testare e forse correggere
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_USERID = "sub";
    static final String CLAIM_KEY_USERNAME = "name";
    static final String CLAIM_KEY_EMAIL = "email";
    static final String CLAIM_KEY_CREATED = "iat";

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public JwtUser getUserDetails(String token) {

        if(token == null){
            return null;
        }
        try {
            final Claims claims = getClaimsFromToken(token);

            return new JwtUser(
                    Long.parseLong(claims.getSubject()),
                    (String) claims.get(CLAIM_KEY_USERNAME),
                    (String) claims.get(CLAIM_KEY_EMAIL),
                    null
            );
        } catch (Exception e) {
            return null;
        }

    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedEncodingException e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) throws JsonProcessingException {
        Map<String, Object> claims = new HashMap<>();
        JwtUser user = (JwtUser) userDetails;
        claims.put(CLAIM_KEY_USERID, user.getId());
        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
        claims.put(CLAIM_KEY_EMAIL, user.getEmail());
        claims.put(CLAIM_KEY_CREATED, System.currentTimeMillis() / 1000L);

        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"))
                    .compact();
        } catch(UnsupportedEncodingException e) {}
        return null;
    }

    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        return !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, System.currentTimeMillis() / 1000L);
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        return (username != null && !isTokenExpired(token));
    }
}