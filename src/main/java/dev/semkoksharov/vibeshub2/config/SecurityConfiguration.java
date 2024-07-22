package dev.semkoksharov.vibeshub2.config;

import dev.semkoksharov.vibeshub2.authentication.AuthFilterJWT;
import dev.semkoksharov.vibeshub2.authentication.UserDetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserDetService userDetailsService;
    private final AuthFilterJWT authFilterJWT;

    @Autowired
    public SecurityConfiguration(UserDetService userDetailsService, AuthFilterJWT authFilterJWT) {
        this.userDetailsService = userDetailsService;
        this.authFilterJWT = authFilterJWT;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/signup/**", "/login/**", "/**","/api/songs/upload", "/api/stream/**").permitAll();
//                    registry.requestMatchers("/api/home/admin/**").hasRole("ADMIN");
//                    registry.requestMatchers("/api/home/user/**").hasRole("USER");
                    registry.anyRequest().authenticated();

                }).formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(authFilterJWT, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


}
