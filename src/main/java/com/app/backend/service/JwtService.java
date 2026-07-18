package com.app.backend.service;

import com.app.backend.config.AppProperties;
import com.app.backend.entity.Role;
import com.app.backend.entity.User;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.utils.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final AppProperties appProperties;


    private SecretKey getSignInKey(String secret){
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    private String buildToken(User user, String secret, long expireSession, String type){
        return Jwts.builder()
                .subject(user.getUsername())
                .signWith(getSignInKey(secret))
                .claim("userId", user.getId())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles().stream().map(
                        Role::getName
                ).toList())
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireSession * 1000))
                .compact();
    }

    public String generateAccesstoken(User user){
        return buildToken(user, (appProperties.getJwt().getAccesssecret()), appProperties.getJwt().getAccessTokenExpireSeconds(), Constant.TYPE_ACCESS_TOKEN);
    }

    public String generateRefreshToken(User user){
        return buildToken(user, appProperties.getJwt().getRefreshsecret(), appProperties.getJwt().getRefreshTokenExpireSeconds(), Constant.TYPE_REFRESH_TOKEN);
    }

    public Claims parseClaim(String token, String type){
        try {
            String secret = getSecretToken(type);

            return Jwts.parser()
                    .verifyWith(getSignInKey(secret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            throw new CommonException(ErrorCode.AUTH_TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new CommonException(ErrorCode.AUTH_TOKEN_INVALID);
        }
    }

    // select cai type token
    private String getSecretToken(String type){
        return switch (type){
            case Constant.TYPE_ACCESS_TOKEN -> appProperties.getJwt().getAccesssecret();
            case Constant.TYPE_REFRESH_TOKEN -> appProperties.getJwt().getRefreshsecret();
            default -> throw new IllegalArgumentException("Unsupported token type: " + type);
        };
    }


    public boolean isExpired(String token, String type){
        return parseClaim(token,(type)).getExpiration().before(new Date());
    }

    public Date getExpiration(String token, String typeToken){

        return parseClaim(token, typeToken).getExpiration();
    }


    public String extractUsername(String token){
        return parseClaim(token, Constant.TYPE_ACCESS_TOKEN).getSubject();
    }


    public List<String> extractRoles(String token){
        Claims claims = parseClaim(token,  Constant.TYPE_ACCESS_TOKEN);
        return claims.get("roles",List.class);
    }


    public String extractUsernameByClaim(Claims claims){
        return claims.getSubject();
    }

    public List<String> extractRolesByClaim(Claims claims){
        return claims.get("roles", List.class);
    }

    public Integer extractUserIdByClaims(Claims claims){
        return claims.get("userId", Integer.class);
    }

    public boolean validTokenExpired(String token){
        return !isExpired(token, Constant.TYPE_ACCESS_TOKEN);
    }

    public Claims parseAccessToken(String token) {
        return parseClaim(token, Constant.TYPE_ACCESS_TOKEN);
    }

    public Claims parseRefreshToken(String token) {
        return parseClaim(token, Constant.TYPE_REFRESH_TOKEN);
    }

    public long getRemainingTime(Claims claims) {
        long now = System.currentTimeMillis();
        long exp = claims.getExpiration().getTime();
        return exp - now;
    }
}
