package com.edu.basaoc.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyAuthData {
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private String userId;
}
