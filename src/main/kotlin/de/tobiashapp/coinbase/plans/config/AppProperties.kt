package de.tobiashapp.coinbase.plans.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "app")
@ConstructorBinding
data class AppProperties(
    val coinbase: CoinbaseProperties,
    val mail: MailProperties,
)

data class CoinbaseProperties(
    val apiKey: String,
    val baseUrl: String,
    val passphrase: String,
    val secret: String,
)

data class MailProperties(val sender: String)
