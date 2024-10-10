package com.ipor.ticketsystem.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UsernameNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("redirect:/login"); // Redirigir a login
        modelAndView.addObject("error", ex.getMessage()); // Si necesitas pasar un mensaje
        return modelAndView;
    }
}
