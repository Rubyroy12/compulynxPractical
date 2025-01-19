package ibrahim.compulynxtest.Auntentication.service.impl;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

import ibrahim.compulynxtest.Auntentication.service.JwtService;
import ibrahim.compulynxtest.Auntentication.service.TokenBlacklistManager;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${token.secret.key}")
    private String jwtSigningKey;
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;


    private final TokenBlacklistManager tokenBlacklistManager;



    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token)
                && !tokenBlacklistManager.isTokenBlacklisted(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    //to be revisited....deprecation
    public Claims extractAllClaims(String token) {
        try {
            System.out.println("Token: " + token);
            System.out.println("Signing Key: " + Base64.getEncoder().encodeToString(getSigningKey().getEncoded()));
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("JWT Token is expired: " + e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.JwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void invalidateToken(String token) {
        tokenBlacklistManager.invalidateToken(token);
    }


}
