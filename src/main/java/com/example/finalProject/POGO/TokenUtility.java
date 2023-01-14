package com.example.finalProject.POGO;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class TokenUtility {

	public static final String keySign = "Test";
	
		
	public String generateToken(String username) {
			
			Map<String, Object> test = new HashMap<String, Object>();
			test.put("username", username);
	
	
			return Jwts.builder().setClaims(test)
					
					.signWith(SignatureAlgorithm.HS512, keySign).compact();
		}
	
	
	 public static Result checkToken(String token) {
		Result result = new Result();

		Map<String, Object> resultMap = new HashMap<>();
		try {
			Claims s = Jwts.parser().setSigningKey(keySign).parseClaimsJws(token).getBody();
			result.setStatusCode("0");
			result.setStatusDescription("Succ");
			String username = (String) s.get("username");
			resultMap.put("username", username);
			result.setResultMap(resultMap);
			return result;
		} catch (SignatureException ex) {
			result.setStatusCode("1");
			result.setStatusDescription("Failed");
			resultMap.put("error", "Invalid JWT signature");
			result.setResultMap(resultMap);
			return result;

		} catch (MalformedJwtException ex) {
			result.setStatusCode("Failed");
			resultMap.put("error", "Invalid JWT token");
			result.setResultMap(resultMap);
			return result;

		} catch (ExpiredJwtException ex) {
			result.setStatusCode("1");
			result.setStatusDescription("Failed");
			resultMap.put("error", "Expired JWT token");
			result.setResultMap(resultMap);
			return result;

		} catch (UnsupportedJwtException ex) {
			result.setStatusCode("1");
			result.setStatusDescription("Failed");
			resultMap.put("error", "Unsupported JWT token");
			result.setResultMap(resultMap);
			return result;

		}

		catch (IllegalArgumentException ex) {
			result.setStatusCode("1");
			result.setStatusDescription("Failed");
			resultMap.put("error", "JWT string is empty");
			result.setResultMap(resultMap);
			return result;

		} catch (Exception e) {
			result.setStatusCode("1");
			result.setStatusDescription("Failed");
			resultMap.put("error", e.getMessage());
			result.setResultMap(resultMap);
			return result;
		}

	}

	 
	
	

}
