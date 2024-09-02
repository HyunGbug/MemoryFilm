/*
 * package com.film.www.util;
 * 
 * import java.util.Date;
 * 
 * import org.springframework.stereotype.Component;
 * 
 * @Component public class JwtTokenUtil {
 * 
 * private final String JWT_SECRET = "your-secret-key"; private final long
 * ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 15; // 15분 private final long
 * REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7일
 * 
 * public String createAccessToken(int userId, String username) { Date now = new
 * Date(); Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);
 * 
 * return Jwts.builder() .setSubject(username) .claim("userId", userId)
 * .setIssuedAt(new Date()) .setExpiration(expiryDate)
 * .signWith(SignatureAlgorithm.HS512, JWT_SECRET) .compact(); }
 * 
 * public String createRefreshToken(Long userId, String username) { Date now =
 * new Date(); Date expiryDate = new Date(now.getTime() +
 * REFRESH_TOKEN_EXPIRATION);
 * 
 * return Jwts.builder() .setSubject(username) .claim("userId", userId)
 * .setIssuedAt(new Date()) .setExpiration(expiryDate)
 * .signWith(SignatureAlgorithm.HS512, JWT_SECRET) .compact(); }
 * 
 * public Long getUserIdFromToken(String token) { Claims claims = Jwts.parser()
 * .setSigningKey(JWT_SECRET) .parseClaimsJws(token) .getBody();
 * 
 * return claims.get("userId", Long.class); }
 * 
 * public boolean validateToken(String token) { try {
 * Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token); return true; }
 * catch (Exception e) { return false; } } }
 */