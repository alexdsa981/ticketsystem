package com.ipor.ticketsystem.api_code.dto.dynamic;

import com.ipor.ticketsystem.model.dynamic.Usuario;

public record UsuarioRecord(
        Long id,
        String username,
        String password,  // Incluimos el campo password
        String nombre,
        Long rolUsuarioId
) {
    public UsuarioRecord(Usuario usuario) {
        this(usuario.getId(), usuario.getUsername(), null, usuario.getNombre(), usuario.getRolUsuario().getId());
    }
}
