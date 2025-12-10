package com.myvalut.envalut_backend.service;
import com.myvalut.envalut_backend.dto.LoginRequestDto;
import com.myvalut.envalut_backend.dto.RegisterDto;
import com.myvalut.envalut_backend.model.User;
import com.myvalut.envalut_backend.repository.UserRepository;
import com.myvalut.envalut_backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public AuthService(UserRepository userRepository,JwtUtil jwtUtil,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.jwtUtil=jwtUtil;
        this.passwordEncoder=passwordEncoder;
    }
    public ResponseEntity<?> createNewUser(RegisterDto newUser) {
        Optional<User> exisitingUser=userRepository.findByEmail(newUser.getEmail());

        if(exisitingUser.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Email Already Exits"));
        }
        User user=new User();
        user.setUserName(newUser.getUserName());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setCreated_at(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Account Created Successful"));
    }

    public ResponseEntity<?> loginExistingUser(LoginRequestDto loginUser) {
        Optional<User>existingUser=userRepository.findByEmail(loginUser.getEmail());
        System.out.println("step 1");
        if(existingUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Invalid Email or Password"));
        }
        System.out.println("step 2");
        User user=existingUser.get();
        if(!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())||!user.getEmail().equals(loginUser.getEmail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Invalid Email or Password"));
        System.out.println("password verified");
        String token=jwtUtil.generateToken(loginUser.getEmail());
        System.out.println("jwt created");

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token",token));
    }
}
