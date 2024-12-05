package com.mini.domain.user.dto;

import lombok.Getter;

@Getter
public class UserDTO {

    private String username;
    private String name;
    private String role;

    public UserDTO(String username, String name, String role) {
        this.username = username;
        this.name = name;
        this.role = role;
    }

    public UserDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
