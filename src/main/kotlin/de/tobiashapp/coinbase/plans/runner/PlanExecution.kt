package de.tobiashapp.coinbase.plans.runner

import de.tobiashapp.coinbase.plans.models.CryptoCurrency
import de.tobiashapp.coinbase.plans.models.FiatCurrency
import java.math.BigDecimal

data class PlanExecution(
    val amount: BigDecimal,
    val cryptoCurrency: CryptoCurrency,
    val fiatCurrency: FiatCurrency,
    val receiverAddress: String,
)
