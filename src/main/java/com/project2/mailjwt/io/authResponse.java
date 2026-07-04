package com.project2.mailjwt.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class authResponse {

    private String email;
    private String token;


}
