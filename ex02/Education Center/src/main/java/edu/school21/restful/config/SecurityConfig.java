package edu.school21.restful.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import edu.school21.restful.models.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.Assert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public SecretKeySpec key() throws NoSuchAlgorithmException, InvalidKeyException {
        Assert.isTrue(secret.length() >= 32, "The secret length must be at least 32 symbols");
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256.init(secretKey);
        return secretKey;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests(auth -> auth
                        .antMatchers("/", "/webjars/**", "/explorer/**", "/docs/**").permitAll()
                        .antMatchers("/signUp").not().authenticated()
                        .mvcMatchers(HttpMethod.DELETE, "/courses/**", "/users/**", "/lessons/**")
                        .hasAuthority(Role.ADMINISTRATOR.name())
                        .mvcMatchers(HttpMethod.POST, "/courses/**", "/users/**", "/lessons/**")
                        .hasAuthority(Role.ADMINISTRATOR.name())
                        .mvcMatchers(HttpMethod.PUT, "/courses/**", "/users/**", "/lessons/**")
                        .hasAuthority(Role.ADMINISTRATOR.name())
                        .mvcMatchers(HttpMethod.PATCH, "/courses/**", "/users/**", "/lessons/**")
                        .hasAuthority(Role.ADMINISTRATOR.name())
                        .anyRequest().hasAnyAuthority(Role.STUDENT.name(), Role.ADMINISTRATOR.name(), Role.TEACHER.name())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth -> oauth.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                ))
                .build();
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("");
        jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException, InvalidKeyException {
        return NimbusJwtDecoder.withSecretKey(key()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws NoSuchAlgorithmException, InvalidKeyException {
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<SecurityContext>(key());
        return new NimbusJwtEncoder(immutableSecret);
    }
}
