package xyz.anycourse.app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import xyz.anycourse.app.domain.dto.TokenValidationDTO;
import xyz.anycourse.app.domain.dto.UserLoginDTO;
import xyz.anycourse.app.service.contract.JwtService;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    public String generateToken(UserLoginDTO user) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .subject(user.getEmail())
                .claims(claims)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractSubject(token);

        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public boolean isTokenExpired(TokenValidationDTO tokenValidationDTO) {
        final String token = tokenValidationDTO.getToken();

        try {
            return isTokenExpired(token);
        } catch (Exception ex) {
            return true;
        }
    }

    private SecretKeySpec getSigningKey() {
        SignatureAlgorithm sa = SignatureAlgorithm.HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        return secretKeySpec;
    }

    private Claims extractAllClaims(String token) {
        JwtParser jwtParser = Jwts.parser().verifyWith(getSigningKey()).build();

        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (Exception ex) {
            throw new RuntimeException("Could not parse token");
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
}
