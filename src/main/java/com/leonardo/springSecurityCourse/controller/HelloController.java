package com.leonardo.springSecurityCourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    // HttpServletRequest usado para acessar as informacoes do auth que fez ele logar
    // lembrando que sempre passa a key no cabecalho da aplicacao que esta segura
    public String greet(HttpServletRequest request) {
        return "Welcome to Leonardo World " + request.getSession().getId();
    }


}
