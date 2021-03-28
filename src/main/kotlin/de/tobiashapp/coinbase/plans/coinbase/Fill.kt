package de.tobiashapp.coinbase.plans.coinbase

import java.math.BigDecimal
import java.time.Instant

data class Fill(
    val createdAt: Instant,
    val cryptoCurrency: CryptoCurrency,
    val fee: BigDecimal,
    val fiatCurrency: FiatCurrency,
    val orderId: String,
    val price: BigDecimal,
    val settled: Boolean,
    val size: BigDecimal,
)
