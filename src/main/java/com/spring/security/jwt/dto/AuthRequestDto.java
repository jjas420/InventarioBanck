/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.security.jwt.dto;

import lombok.Data;

/**
 *
 * @author ayosu
 */
@Data
public class AuthRequestDto {

    String user;
    String password;

}
