package de.tobiashapp.coinbase.plans.config

import com.coinbase.exchange.api.exchange.CoinbaseExchange
import com.coinbase.exchange.api.exchange.CoinbaseExchangeImpl
import com.coinbase.exchange.api.orders.OrderService
import com.coinbase.exchange.security.Signature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoinbaseConfig {
    @Bean
    fun signature(coinbaseProperties: CoinbaseProperties): Signature {
        return Signature(coinbaseProperties.secret)
    }

    @Bean
    fun coinbasePro(
        coinbaseProperties: CoinbaseProperties,
        signature: Signature,
        objectMapper: ObjectMapper,
    ): CoinbaseExchange {
        return CoinbaseExchangeImpl(
            coinbaseProperties.apiKey,
            coinbaseProperties.passphrase,
            coinbaseProperties.baseUrl,
            signature,
            objectMapper,
        )
    }

    @Bean
    fun orderService(exchange: CoinbaseExchange): OrderService {
        return OrderService(exchange)
    }
}
