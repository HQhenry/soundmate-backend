package com.edu.basaoc.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyLoginRequest {
    private String authCode;
    private String redirectUri;
}
