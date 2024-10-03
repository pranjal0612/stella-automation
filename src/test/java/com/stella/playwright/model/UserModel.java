package com.stella.playwright.model;

import lombok.Data;

@Data
public class UserModel {
    private Integer userId;
    private String name;
    private String email;
    private String password;
    private String newPassword;
    private String confirmPassword;
    private String image;
}
