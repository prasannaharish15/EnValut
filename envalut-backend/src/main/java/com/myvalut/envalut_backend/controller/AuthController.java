package com.myvalut.envalut_backend.controller;

import com.myvalut.envalut_backend.dto.LoginRequestDto;
import com.myvalut.envalut_backend.dto.RegisterDto;
import com.myvalut.envalut_backend.model.User;
import com.myvalut.envalut_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/createnewuser")
    public ResponseEntity<?> createNewUser(@RequestBody RegisterDto newUser){
        return authService.createNewUser(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginExistingUser(@RequestBody LoginRequestDto loginUser){
        return authService.loginExistingUser(loginUser);
    }


}
