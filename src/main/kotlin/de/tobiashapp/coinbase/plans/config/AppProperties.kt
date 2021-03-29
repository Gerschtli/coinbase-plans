package de.tobiashapp.coinbase.plans.config

import de.tobiashapp.coinbase.plans.models.CryptoCurrency
import de.tobiashapp.coinbase.plans.models.FiatCurrency
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.math.BigDecimal

@ConfigurationProperties(prefix = "app")
@ConstructorBinding
data class AppProperties(
    val coinbase: CoinbaseProperties,
    val mail: MailProperties,
    val plans: List<PlanProperties>?,
)

data class CoinbaseProperties(
    val apiKey: String,
    val baseUrl: String,
    val passphrase: String,
    val secret: String,
)

data class MailProperties(val sender: String)

data class PlanProperties(
    val amount: BigDecimal,
    val cron: String,
    val cryptoCurrency: CryptoCurrency,
    val fiatCurrency: FiatCurrency,
    val receiverAddress: String,
)
