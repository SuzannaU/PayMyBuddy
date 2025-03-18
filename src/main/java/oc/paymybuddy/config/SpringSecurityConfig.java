package oc.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private CustomUserDetailsService customUserDetailsService;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/").permitAll();
                    auth.requestMatchers("/error").permitAll();
                    auth.requestMatchers("/css/**").permitAll();
                    auth.requestMatchers("/register").permitAll();
                    auth.requestMatchers("/add-relation").hasRole("USER");
                    auth.requestMatchers("/profile").hasRole("USER");
                    auth.requestMatchers("/transfer").hasRole("USER");
                    auth.anyRequest().authenticated();
                })
                .formLogin(form->form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform-login")
                        .defaultSuccessUrl("/transfer", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        return authenticationProvider;
    }
}
