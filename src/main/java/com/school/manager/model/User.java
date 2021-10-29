package com.school.manager.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Data
public class User {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String username;
    private String password;
    private Integer role;
    private String phone;
    private String email;
    private Integer status;

}
