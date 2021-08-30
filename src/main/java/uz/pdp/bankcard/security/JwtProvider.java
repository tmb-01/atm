package uz.pdp.bankcard.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.bankcard.entity.Role;

import java.util.Date;
import java.util.Set;

//import io.jsonwebtoken.Jwts;


@Component
public class JwtProvider {

    private static final long EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;
    private static final String KEY = "maraim01";

    public String generateToken(String username, Set<Role> role) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, KEY)
                .claim("roles", role)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }


}
