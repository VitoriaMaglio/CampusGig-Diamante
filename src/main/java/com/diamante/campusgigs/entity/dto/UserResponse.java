package com.diamante.campusgigs.entity.dto;

import com.diamante.campusgigs.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String city;
    private String state;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .city(user.getCity())
                .state(user.getState())
                .build();
    }
}