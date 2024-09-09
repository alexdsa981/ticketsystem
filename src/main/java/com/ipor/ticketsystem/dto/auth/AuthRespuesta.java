package com.ipor.ticketsystem.dto.auth;

import lombok.Data;

//esta clase va a ser la que nos devolverá la informacion con el token y el tipo que tenga este
@Data
public class AuthRespuesta {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthRespuesta(String accessToken){
        this.accessToken = accessToken;
    }
}
