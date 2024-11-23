package com.chatop.backend.dto;

import java.util.Date;

import com.chatop.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserResponseDto {

    private int id;
    private String name;
    private String email;
    @JsonFormat(pattern = "yyyy-dd-MM")
    private Date created_at;
    @JsonFormat(pattern = "yyyy-dd-MM")
    private Date updated_at;

    public UserResponseDto(User user) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        created_at = user.getCreated_at();
        updated_at = user.getUpdated_at();
    }
}
