package com.ipor.ticketsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    //con este bean nos encargaremos de encriptar todas las contraseñas
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //este bean se va a encargar de verificar la informacion de los usuarios que se logearan en nuestra api
    @Bean
    AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    //este bean incorporará el filtro de seguridad de json web token que creamos en nuestra clase anterior
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    //se crea un bean que establecerá una cadena de filtros de seguridad en nuestra app, y es aqui donde determianremos los permisos de usuarios en nuestra aplicacion
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Todos pueden logearse, sin necesidad de autorización "token"
                        .requestMatchers("/api/login/**").permitAll()
                        // Permitir a los administradores realizar todas las operaciones en la ruta /api/**
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("Admin")
                        .requestMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("Admin")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority("Admin")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority("Admin")

                        //permitir a los usuarios crear tickets y ver los suyos
//                        .requestMatchers(HttpMethod.POST, "/api/tickets/**").hasAnyAuthority("Usuario")
//                        .requestMatchers(HttpMethod.GET, "/api/tickets/**").hasAnyAuthority("Usuario")

                        .anyRequest().authenticated())
                .httpBasic(withDefaults());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
