package com.example.demo.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(source).stream(),
                extractAuthorities(source).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                source,
                authorities,
                getPrincipleClaimName(source)
        );
    }

    private String getPrincipleClaimName(Jwt source) {
        String claimNames = JwtClaimNames.SUB;
        if (principleAttribute != null) {
            claimNames = principleAttribute;
        }
        return source.getClaim(claimNames);
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(Jwt source) {
        Map<String, Object> realmAccess;
        Collection<String> resourceRoles;
        if (source.getClaim("realm_access") == null) {
            return Set.of();
        }
        realmAccess = source.getClaim("realm_access");
        resourceRoles = (Collection<String>) realmAccess.get("roles");
        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
