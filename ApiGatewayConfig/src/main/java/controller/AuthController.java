package controller;

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

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        // Hardcoded validation for demonstration
        if ("admin".equals(authRequest.getUsername()) && "password".equals(authRequest.getPassword())) {
            String token = jwtUtil.generateToken(authRequest.getUsername());
            return new AuthResponse(token);
        } else {
            throw new RuntimeException("Invalid access");
        }
    }
}
