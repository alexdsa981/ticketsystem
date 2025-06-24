package com.ipor.ticketsystem.usuario.login;

import com.ipor.ticketsystem.core.other.CookieUtil;
import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.usuario.UsuarioRepository;
import com.ipor.ticketsystem.usuario.UsuarioService;
import com.ipor.ticketsystem.usuario.rol.RolUsuarioRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/app")
public class LoginController {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    LoginService loginService;


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws IOException {
        return loginService.logearUsuarioAlSistema(username, password, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) throws IOException {
        CookieUtil.removeJwtCookie(response);
        response.sendRedirect("/login");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(@RequestParam String newPassword) {
        Long iDUsuarioLogeado = usuarioService.getIDdeUsuarioLogeado();
        Usuario usuario = usuarioService.getUsuarioPorId(iDUsuarioLogeado);
            usuario.asignarYEncriptarPassword(newPassword);
            usuario.setChangedPass(true);
            usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok().build();

    }


}
