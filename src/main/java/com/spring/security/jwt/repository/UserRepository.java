/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.security.jwt.repository;

import com.spring.security.jwt.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ayosu
 */
@Repository
public class UserRepository  implements IUserRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public UserModel findByName(String user) {
        String SQL = "SELECT * FROM users WHERE name = ?";
        return jdbcTemplate.queryForObject(SQL, new Object[]{user},
                
                new BeanPropertyRowMapper<>(UserModel.class));
    }
}
