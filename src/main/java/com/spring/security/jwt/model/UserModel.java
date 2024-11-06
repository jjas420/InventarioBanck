/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.security.jwt.model;

import lombok.Data;

/**
 *
 * @author ayosu
 */
@Data
public class UserModel {
    Integer user_id;
    String name;
    String password;
    String phone;
    
    
}
