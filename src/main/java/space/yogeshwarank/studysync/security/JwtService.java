package space.yogeshwarank.studysync.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.yogeshwarank.studysync.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


/**
 * Service responsible for creating JWT tokens for registered users
 *
 * @author Yogeshwaran K
 * @since 18.09.2026
 */

@Service
public class JwtService {


    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(@Value("${JWT_SECRET}") String secret,
                      @Value("${JWT_EXPIRATION}") long expiration){

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }


    /**
     * Generates the jwt token for the authenticated user
     *
     * @param user
     * @return jwt token as a string
     */
    public String generateToken(User user){

        Date now = new Date();
        Date expiryDate = new Date( now.getTime() + expiration );

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }


    public String extractUserId(String accessToken){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .getSubject();
    }


    public boolean isTokenValid(String accessToken){

        try{
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken);

            return true;
        }
        catch(Exception exception){
            return false;
        }

    }


}
