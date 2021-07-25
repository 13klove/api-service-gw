package com.service.gateway.filter

import com.service.gateway.component.TokenProvider
import mu.KLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class TokenRevokeFilter(
    private val tokenProvider: TokenProvider
): AbstractGatewayFilterFactory<Any>() {

    companion object: KLogging() {
        const val AUTHORIZATION = "Authorization"
        const val X_HEADER_USER_MAIL = "X-HEADER-USER-MAIL"
    }

    override fun apply(config: Any): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val request = exchange.request
            val headerToken = request.headers[AUTHORIZATION]?.get(0)

            if (headerToken.isNullOrBlank()) {
                throw ResponseStatusException(UNAUTHORIZED, "no permission")
            }

            val token = headerToken.replace("Bearer ", "")
            val email = tokenProvider.checkToken(token)
            logger.info { "token in mail: $email" }

            request.mutate().headers { headers ->
                headers[X_HEADER_USER_MAIL] = email
            }

            chain.filter(exchange)
        }
    }

}