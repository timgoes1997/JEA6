package com.github.timgoes1997.java.authentication.token;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.user.User;
import io.jsonwebtoken.*;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.joining;

@Stateless
public class TokenProvider {

    @Inject
    private Logger logger;

    @Inject
    private UserDAO userDAO;

    private static final String AUTHORITIES_KEY = "auth";

    //The secret key used for encryption
    private String secretKey;
    //How long the token is valid when the user hasn't checked remember me
    private long tokenValidity;
    //Hoe long the token is valid when a user has checked remember me
    private long tokenValidityForRememberMe;

    @PostConstruct
    public void init() {
        this.secretKey = "Kwetter-tim-goes";
        this.tokenValidity = TimeUnit.HOURS.toMillis(10);
        this.tokenValidityForRememberMe = TimeUnit.SECONDS.toMillis(Constants.REMEMBERME_VALIDITY_SECONDS);
    }

    public String issueToken(String username, Set<String> authorities, Boolean rememberMe) {
        long now = new Date().getTime();
        long validity = rememberMe ? tokenValidityForRememberMe : tokenValidity; //if rememberMe then use the validity for rememberMe otherwise 10 hours.

        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authorities.stream().collect(joining(",")))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(new Date(now + validity))
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);

            return true;
        } catch (SignatureException e) {
            logger.log(Level.INFO, "Invalid JWT signature: {0}", e.getMessage());
            return false;
        }
    }

    public User extractUser(String authToken){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);

            return userDAO.findByUsername(claims.getBody().getSubject());
        } catch (SignatureException e) {
            logger.log(Level.INFO, "Invalid JWT signature: {0}", e.getMessage());
            return null;
        }
    }
}
