package com.edu.basaoc.model.payload.request;

import lombok.Data;

import java.time.Instant;

@Data
public class SaveAccountRequest {

    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private Instant issuedAt;
}
