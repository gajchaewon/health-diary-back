package com.bodytok.healthdiary.repository.jwt;

import com.bodytok.healthdiary.domain.JwtToken;
import org.springframework.data.repository.CrudRepository;



public interface JwtTokenRepository extends CrudRepository<JwtToken, String> {
}