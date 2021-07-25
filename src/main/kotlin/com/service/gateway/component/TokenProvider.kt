package com.service.gateway.component

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import mu.KLogging
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.security.Key
import java.util.Base64

@Component
class TokenProvider(
    private val environment: Environment
) {

    companion object: KLogging()

    private final val key: Key

    init {
        val secreatKey = environment.getProperty("secret.key")
        key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secreatKey?.toByteArray()))
    }

    fun checkToken(token: String): String {
        if (token.isBlank()) {
            throw IllegalArgumentException("bad request and no permission")
        }

        val body = parseClaims(token)
        return body["sub"].toString()
    }

    fun parseClaims(token: String): Claims
    = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .body

}