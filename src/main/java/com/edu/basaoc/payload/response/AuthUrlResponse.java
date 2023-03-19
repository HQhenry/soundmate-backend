package com.edu.basaoc.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URI;

@Getter
@AllArgsConstructor
public class AuthUrlResponse {
    private URI uri;
}
