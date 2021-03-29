package de.tobiashapp.coinbase.plans.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "app.coinbase")
@ConstructorBinding
data class CoinbaseProperties(
    val apiKey: String,
    val baseUrl: String,
    val passphrase: String,
    val secret: String,
)
