package de.tobiashapp.coinbase.plans.config

import com.coinbase.exchange.api.exchange.CoinbaseExchange
import com.coinbase.exchange.api.exchange.CoinbaseExchangeImpl
import com.coinbase.exchange.api.orders.OrderService
import com.coinbase.exchange.security.Signature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoinbaseConfig {
    @Bean
    fun signature(@Value("\${app.coinbase.auth.secret}") secret: String): Signature {
        return Signature(secret)
    }

    @Bean
    fun coinbasePro(
        @Value("\${app.coinbase.auth.api-key}") publicKey: String,
        @Value("\${app.coinbase.auth.passphrase}") passphrase: String,
        @Value("\${app.coinbase.api.base-url}") baseUrl: String,
        signature: Signature,
        objectMapper: ObjectMapper,
    ): CoinbaseExchange {
        return CoinbaseExchangeImpl(publicKey, passphrase, baseUrl, signature, objectMapper)
    }

    @Bean
    fun orderService(exchange: CoinbaseExchange): OrderService {
        return OrderService(exchange)
    }
}
