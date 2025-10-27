package com.example.ProjetoFinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. DESABILITA O CSRF (OBRIGATÓRIO PARA API REST COM POST)
    // 2. CONFIGURA AS REQUISIÇÕES (Basic Auth no caso)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // <--- DESABILITA O CSRF!
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated() // Protege todas as outras rotas
                )
                .httpBasic(httpBasic -> {}) // Habilita o Basic Auth (username:user, password:...)
                .build();
    }

    // Mantém o usuário "user" em memória (igual ao padrão que você estava usando)
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("123456789") // Coloque uma senha mais fácil de lembrar para testar!
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}