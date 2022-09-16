package com.edu.basaoc.payload;

import lombok.Data;

@Data
public class SignupRequest {

    private String accessToken;
    private String tokenType;
    private String scope;
    private int expiresIn;
    private String refreshToken;
}
