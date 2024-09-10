package com.film.www.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String profileImageUrl;  // 필요할 때만 사용
    private String memo;             // 필요할 때만 사용

    // getters and setters
}
