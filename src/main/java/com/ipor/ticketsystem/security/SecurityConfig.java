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

    //este bean se va a encargar de verificar la informacion de los usuarios que se logearan en nuestra app
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //este bean incorporará el filtro de seguridad de json web token que creamos en nuestra clase anterior
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
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
                        .requestMatchers("/").permitAll()
                        // Permitir a todos loguearse
                        .requestMatchers("/app/login/**").permitAll()
                        //paginas front
                        .requestMatchers("/fragment-expression/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/inicio/**").hasAnyAuthority("Usuario", "Admin", "Soporte")
                        //poner roles más especificos primero para no sobreponer los permisos globales de los admin/soporte
                        // Permitir a los usuarios crear tickets y ver los suyos
                        .requestMatchers(HttpMethod.POST, "/app/tickets/**").hasAnyAuthority("Usuario", "Admin", "Soporte")
                        .requestMatchers(HttpMethod.GET, "/app/tickets/**").hasAnyAuthority("Usuario", "Admin", "Soporte")

                        //permitir a los usuarios subir y ver sus archivos adjuntos
                        .requestMatchers(HttpMethod.POST, "/app/archivosAdjuntos/**").hasAnyAuthority("Admin", "Soporte", "Usuario")
                        .requestMatchers(HttpMethod.GET, "/app/archivosAdjuntos/**").hasAnyAuthority("Admin", "Soporte", "Usuario")

                        //permitir a soporte recepcionar y atender tickets
                        .requestMatchers(HttpMethod.POST, "/app/recepciones/**").hasAnyAuthority("Admin", "Soporte")
                        .requestMatchers(HttpMethod.GET, "/app/recepciones/**").hasAnyAuthority("Admin", "Soporte")
                        .requestMatchers(HttpMethod.POST, "/app/servicios/**").hasAnyAuthority("Admin", "Soporte")
                        .requestMatchers(HttpMethod.GET, "/app/servicios/**").hasAnyAuthority("Admin", "Soporte")

                        // Permitir a los administradores realizar todas las operaciones en la ruta /app/**
                        .requestMatchers(HttpMethod.GET, "/app/**").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.POST, "/app/**").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.PUT, "/app/**").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.DELETE, "/app/**").hasAuthority("Admin")
                        .requestMatchers("/app/usuarios/**").hasAuthority("Admin")


                        // Cualquier otra solicitud debe estar autenticada
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
