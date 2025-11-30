package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/token")
    public AuthResponse getToken(@RequestBody AuthRequest authRequest) {
        // In a real app, we would authenticate the user against a database here.
        // For now, we just generate the token for the given username.
        String token = jwtUtil.generateToken(authRequest.getUsername());
        return new AuthResponse(token);
    }
}
