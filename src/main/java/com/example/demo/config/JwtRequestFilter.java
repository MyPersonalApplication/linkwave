package com.example.demo.config;

import com.example.demo.config.authentication.AccessTokenHandler;
import com.example.demo.model.User;
import com.example.demo.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.ws.rs.NotAuthorizedException;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AccessTokenHandler accessTokenHandler;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestUri = request.getRequestURI();
        if (!requestUri.startsWith("/api/") || (requestUri.startsWith("/api/auth/") && !"/api/user/change-password".equals(requestUri)
                && !"/api/user/profile".equals(requestUri))) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            parseAndSetAuthentication(request);
        } catch (ExpiredJwtException e) {
            logger.error("Cannot set user authentication: {}", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(convertObjectToJson(e));
        } catch (NotAuthorizedException e) {
            logger.error("Cannot set user authentication: {}", e);
            throw new NotAuthorizedException("NOT_AUTHORIZED", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private void parseAndSetAuthentication(HttpServletRequest request) throws JsonProcessingException {
        String jwtString = parseJwt(request);
        if (StringUtils.hasLength(jwtString)) {
            Jwt jwt = decodeJwt(jwtString);
            isTokenExpired(jwt);
            String username = jwt.getClaims().get("email").toString();

            accessTokenHandler.setToken(jwtString);
            Optional<User> user = Optional.ofNullable(userService.loadUserByUsername(username));
            if (user.isPresent()) {
                accessTokenHandler.setUsername(username);
                accessTokenHandler.setUserId(user.get().getId());
            } else {
                throw new NotAuthorizedException("USER_NOT_FOUND");
            }
            JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, AuthorityUtils.createAuthorityList(getRoles(jwt.getClaims())));
            SecurityContextHolder.getContext().setAuthentication(token);
        } else {
            throw new NotAuthorizedException("JWT_MISSING");
        }
    }

    private void isTokenExpired(Jwt jwt) {
        Instant expiration = Instant.ofEpochMilli(Long.parseLong(jwt.getClaims().get("exp").toString()) * 1000);
        if (Instant.now().isAfter(expiration)) {
            throw new ExpiredJwtException(null, null, null);
        }
    }

    private Jwt decodeJwt(String jwtString) throws JsonProcessingException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = jwtString.split("\\.");
        String headerString = new String(decoder.decode(chunks[0]));
        String payloadString = new String(decoder.decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> headers = mapper.readValue(headerString, Map.class);
        Map<String, Object> payload = mapper.readValue(payloadString, Map.class);
        Instant iat = Instant.ofEpochMilli(Long.parseLong(payload.get("iat").toString()));
        Instant exp = Instant.ofEpochMilli(Long.parseLong(payload.get("exp").toString()));
        return new Jwt(jwtString, iat, exp, headers, payload);
    }

    private String[] getRoles(Map<String, Object> payload) {
        LinkedHashMap<String, List> realmAccess = (LinkedHashMap<String, List>) payload.get("realm_access");
        ArrayList<String> roles = (ArrayList<String>) realmAccess.get("roles");
        return roles.toArray(String[]::new);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
