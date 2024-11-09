/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.security.jwt.controller;

import com.spring.security.jwt.dto.AuthRequestDto;
import com.spring.security.jwt.dto.AuthResponseDto;
import com.spring.security.jwt.model.UserModel;
import com.spring.security.jwt.repository.UserRepository;
import com.spring.security.jwt.service.JwtUtilService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ayosu
 */
@Controller
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthRequestDto authRequestDto) {
   try {
       
       
       
        //1. Gestion authenticationManager
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestDto.getUser(), authRequestDto.getPassword()
        ));

        //2. Validar el usuario en la bd
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authRequestDto.getUser());
         UserModel userModel = userRepository.findByName(authRequestDto.getUser());


        //3. Generar token
        String jwt = this.jwtUtilService.generateToken(userDetails, userModel.getRole());
        String refreshToken = this.jwtUtilService.generateRefreshToken(userDetails, userModel.getRole());
        
         AuthResponseDto authResponseDto = new AuthResponseDto();
         authResponseDto.setRefreshToken(refreshToken);
            authResponseDto.setToken(jwt);

        return new ResponseEntity<>( authResponseDto ,HttpStatus.OK);
   }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error Authetication:::" + e.getMessage());
        }
      
    }
    
    
    
    @PostMapping("/refresh")
    public ResponseEntity<?> auth(@RequestBody Map<String, String>  request) {
        String refreshToken = request.get("refreshToken");
        try {
            String username = jwtUtilService.extractUsername(refreshToken);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            UserModel userModel = userRepository.findByName(username);

            if(jwtUtilService.validateToken(refreshToken, userDetails)) {
                String newJwt = jwtUtilService.generateToken(userDetails, userModel.getRole());
                String newRefreshToken = jwtUtilService.generateRefreshToken(userDetails, userModel.getRole());

                AuthResponseDto authResponseDto = new AuthResponseDto();
                authResponseDto.setToken(newJwt);
                authResponseDto.setRefreshToken(newRefreshToken);

                return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
            }else {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
            }


        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error refresh token:::" + e.getMessage());
        }

    }

}
