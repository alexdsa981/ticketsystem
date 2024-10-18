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
                        // Funcionalidades de la app permitidas a todos sin loguearse
                        .requestMatchers("/app/login/**").permitAll()
                        .requestMatchers("/app/logout/**").permitAll()
                        //paginas front permitidas sin logearse
                        .requestMatchers("/fragment-expression/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        //permiso para todos
                        .requestMatchers("/app/usuarios/rol").permitAll()
                        .requestMatchers("/common/**").permitAll()
                        //todas las paginas pueden ser accedidas por cualquier usuario logeado, usar el ejemplo de abajo para
                        //paginas front que requieren autorización, el resto de paginas pueden ser accedidas por todos, por ejemplo usuario
                        //puede ingresar a todas las pagians menos las que tengan /soporte/ o /admin/
                        .requestMatchers("/soporte/**").hasAnyAuthority("Admin", "Soporte")
                        .requestMatchers("/admin/**").hasAuthority("Admin")
                        //poner roles más especificos primero para no sobreponer los permisos globales de los admin/soporte
                        // Permitir a los usuarios crear tickets y ver los suyos

                        .requestMatchers("/app/usuarios/**").hasAuthority("Admin")


                        // Cualquier otra solicitud debe estar autenticada
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
