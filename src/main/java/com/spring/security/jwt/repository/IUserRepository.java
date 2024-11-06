/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.UserModel;

/**
 *
 * @author ayosu
 */
public interface IUserRepository {
     public UserModel findByName(String user);
    
}
