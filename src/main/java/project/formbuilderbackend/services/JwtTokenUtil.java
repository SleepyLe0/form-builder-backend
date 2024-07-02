package project.formbuilderbackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("#{${jwt.max-access-token-interval-hour}*60*60*1000}")
    private long JWT_ACCESS_TOKEN_EXP;
    @Value("#{${jwt.max-refresh-token-interval-day}*24*60*60*1000L}")
    private long JWT_REFRESH_TOKEN_EXP;
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public String generateAccessToken(UserDetails userDetails) {
        return doGenerateToken(Map.of(), userDetails, "ACCESS");
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return doGenerateToken(Map.of(), userDetails, "REFRESH");
    }

    public String getTypeFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getHeader().getType();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String doGenerateToken(Map<String, Object> claims, UserDetails userDetails, String tokenType) {
        long expiration = switch (tokenType) {
            case "ACCESS" -> JWT_ACCESS_TOKEN_EXP;
            case "REFRESH" -> JWT_REFRESH_TOKEN_EXP;
            default -> throw new IllegalArgumentException("Invalid token type");
        };
        return Jwts.builder()
                .setHeaderParam("typ", tokenType)
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signatureAlgorithm, SECRET_KEY)
                .compact();
    }

    public Boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        String tokenType = getTypeFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && tokenType.equals("REFRESH"));
    }

    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        String tokenType = getTypeFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && tokenType.equals("ACCESS"));
    }
}