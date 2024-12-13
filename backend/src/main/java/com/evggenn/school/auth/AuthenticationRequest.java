package com.evggenn.school.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
