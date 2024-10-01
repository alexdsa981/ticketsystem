package com.ipor.ticketsystem.api_code.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginEntrada {
    private String username;
    private String password;
}
