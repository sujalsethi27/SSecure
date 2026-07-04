package com.project2.mailjwt.util;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class jwtutil {

@Value("${jwt.secret.key}")
private String SECRET_KEY;

public String generatetoken(UserDetails userDetails){

return Jwts.builder()
.subject(userDetails.getUsername())
.issuedAt(new java.util.Date())
.expiration(
new java.util.Date(
System.currentTimeMillis()+1000*60*60*10
)
)
.signWith(
Keys.hmacShaKeyFor(
SECRET_KEY.getBytes()
)
)
.compact();
}

private Claims extractallclaims(String token){
return Jwts
.parser()
.verifyWith(
Keys.hmacShaKeyFor(
SECRET_KEY.getBytes()
)
)
.build()
.parseSignedClaims(token)
.getPayload();
 // jwt contains three parts header payload and signature so at first the parser creates object of the jwt that we want to read and then we verify the signature of the jwt with the help of the secret key and then we build the parser and then we parse the jwt and then we get the payload of the jwt which contains the claims of the jwt and then we return the claims of the jwt
}

public <T> T extractclaim(String token, java.util.function.Function<Claims, T> claimsResolver){
Claims claims = extractallclaims(token);
return claimsResolver.apply(claims);
}
// this whole block above is a generic method that takes a jwt token and get a claim from the jwt token and then we return the claim of the jwt token and then we can use this method to get any claim from the jwt token by passing the claim resolver function to this method and then we can use this method to get any claim from the jwt token by passing the claim resolver function to this method

public String extractusername(String token){
return extractclaim(token, Claims::getSubject);
}
public Date extractexpiration(String token) {
    return extractclaim(token, Claims::getExpiration);
}
private boolean isTokenExpired(String token) {
    return extractexpiration(token).before(new Date());
}
public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractusername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
}
}