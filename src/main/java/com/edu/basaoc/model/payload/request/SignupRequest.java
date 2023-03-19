package com.edu.basaoc.model.payload.request;

import lombok.Data;

@Data
public class SignupRequest {

    private String accessToken;
    private String tokenType;
    private String scope;
    private int expiresIn;
    private String refreshToken;
}
