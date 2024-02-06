package com.bodytok.healthdiary.dto.auth.request;

import lombok.*;


public record AuthenticationRequest(String email, String password) {
}