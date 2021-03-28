package de.tobiashapp.coinbase.plans.config

import com.coinbase.exchange.api.accounts.AccountService
import com.coinbase.exchange.api.deposits.DepositService
import com.coinbase.exchange.api.exchange.CoinbaseExchange
import com.coinbase.exchange.api.exchange.CoinbaseExchangeImpl
import com.coinbase.exchange.api.orders.OrderService
import com.coinbase.exchange.api.transfers.TransferService
import com.coinbase.exchange.api.withdrawals.WithdrawalsService
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
    fun accountService(exchange: CoinbaseExchange): AccountService {
        return AccountService(exchange)
    }

    @Bean
    fun depositService(exchange: CoinbaseExchange): DepositService {
        return DepositService(exchange)
    }

    @Bean
    fun orderService(exchange: CoinbaseExchange): OrderService {
        return OrderService(exchange)
    }

    @Bean
    fun transferService(exchange: CoinbaseExchange): TransferService {
        return TransferService(exchange)
    }

    @Bean
    fun withdrawalsService(exchange: CoinbaseExchange): WithdrawalsService {
        return WithdrawalsService(exchange)
    }
}
