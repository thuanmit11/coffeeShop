package org.example.coffeshop.config;

import lombok.RequiredArgsConstructor;
import org.example.coffeshop.entities.Role;
import org.example.coffeshop.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/order/place").hasAuthority(Role.CUSTOMER)
                        .requestMatchers("/api/v1/order").hasAnyAuthority(Role.OPERATOR, Role.CUSTOMER)
                        .requestMatchers("/api/v1/order/{orderId}/cancel").hasAuthority(Role.CUSTOMER)
                        .requestMatchers(HttpMethod.GET,"/api/v1/shop").hasAnyAuthority(Role.CUSTOMER, Role.OPERATOR, Role.ADMIN)
                        .requestMatchers("/api/v1/shop/{shopId}/queue").hasAnyAuthority(Role.OPERATOR, Role.CUSTOMER)
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/shop/**").hasAuthority(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT,"/api/v1/shop/**").hasAuthority(Role.ADMIN)
                        .requestMatchers(HttpMethod.POST,"/api/v1/shop/**").hasAuthority(Role.ADMIN)
                        .requestMatchers("/api/v1/order/{orderId}/queue").hasAuthority(Role.OPERATOR)
                        .requestMatchers("/api/v1/order/{orderId}/serve").hasAuthority(Role.OPERATOR)
                        .requestMatchers(HttpMethod.GET, "/api/v1/menu/{shopId}").hasAnyAuthority(Role.OPERATOR, Role.CUSTOMER, Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/menu/{menuId}").hasAuthority(Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/menu/{menuId}").hasAuthority(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/menu/{menuId}").hasAuthority(Role.ADMIN)
                        .anyRequest().authenticated()

                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }
}
