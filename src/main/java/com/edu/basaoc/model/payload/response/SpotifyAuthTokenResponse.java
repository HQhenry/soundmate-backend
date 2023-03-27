package com.edu.basaoc.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SpotifyAuthTokenResponse {
    private final String access_token;
    private final String token_type;
    private final String scope;
    private final Integer expires_in;
    private final String refresh_token;


}
