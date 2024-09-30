package com.ipor.ticketsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//esta clase validará la informacion del token y si esto es exitoso, establecerá la autenticacion de un usuario en la solicitud o en el contexto de seguridad de nuestra aplicacion
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUsersDetailsService customUsersDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


//    private String obtenerTokenDeSolicitud(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText((bearerToken)) && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7, bearerToken.length());
//        }
//        return null;
//    }
private String obtenerTokenDeSolicitud(HttpServletRequest request){
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JWT")) {
                return cookie.getValue();
            }
        }
    }
    return null;
}




    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = obtenerTokenDeSolicitud(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validarToken(token)){
            String username = jwtTokenProvider.obtenerUsernameDeJWT(token);
            UserDetails userDetails = customUsersDetailsService.loadUserByUsername(username);
            List <String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            if (userRoles.contains("Usuario") || userRoles.contains("Soporte") || userRoles.contains("Admin")){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
