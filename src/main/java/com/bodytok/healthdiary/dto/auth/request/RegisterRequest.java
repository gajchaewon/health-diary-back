package com.bodytok.healthdiary.dto.auth.request;


import lombok.*;


@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String nickname;
    private Byte[] profileImage;
}
